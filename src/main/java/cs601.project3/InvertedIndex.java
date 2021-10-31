package cs601.project3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Building inverted index,and support searching
 *
 * @param <T> generic type of object
 */
public class InvertedIndex<T> {

    private final List<T> documentID;
    private final Map<String, List<Map.Entry<Integer, Integer>>> index;

    /**
     * constructor, build inverted index
     *
     * @param map reviewAsin or qaAsin HashMap
     */
    public InvertedIndex(Map<String, List<T>> map) {
        this.index = new HashMap<>();
        this.documentID = new ArrayList<>();
        buildIndex(map);

    }

    /**
     * build index for each single word
     *
     * @param map reviewAsin or qaAsin HashMap
     */
    public void buildIndex(Map<String, List<T>> map) {
        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            for (T t : entry.getValue()) {
                documentID.add(t);
                // reference: https://www.studytonight.com/java-examples/how-to-remove-punctuation-from-string-in-java
                String[] words = t.toString().toLowerCase().replaceAll("\\p{Punct}", "").split(" ");
                List<String> wordList = new ArrayList<>();
                Collections.addAll(wordList, words);
                for (String term : wordList.stream().distinct().toList()) {
                    if (term.isEmpty() || term.isBlank()) {
                        continue;
                    }
                    if (!index.containsKey(term)) {
                        this.index.put(term, new ArrayList<>());
                    }
                    this.index.get(term).add(Map.entry(documentID.size() - 1, Collections.frequency(wordList, term)));
                }
            }
        }
        for (String key : index.keySet()) {
            this.index.get(key).sort(Comparator.comparing(e -> -e.getValue()));
        }
    }

    /**
     * Return a list that contains all the objects that including search term
     *
     * @param term the word that users want to search
     * @return a list that contains all Amazon objects that the term corresponding to
     */
    public List<T> search(String term) {
        if (!this.index.containsKey(term.toLowerCase())) {
            return null;
        }

        List<T> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : index.get(term.toLowerCase())) {
            result.add(documentID.get(entry.getKey()));
        }
        return result;
    }

}

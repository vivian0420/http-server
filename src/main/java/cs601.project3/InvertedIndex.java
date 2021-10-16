package cs601.project3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex<T> {

    private List<T> documentID;
    private final Map<String, List<Map.Entry<Integer, Integer>>> index;

    public InvertedIndex(Map<String, List<T>> map) {
        this.index = new HashMap<>();
        this.documentID = new ArrayList<>();
        buildIndex(map);
    }

    public void buildIndex(Map<String, List<T>>map) {
        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            for (T t : entry.getValue()) {
                documentID.add(t);
                // reference: https://www.studytonight.com/java-examples/how-to-remove-punctuation-from-string-in-java
                String[] words = t.toString().toLowerCase().replaceAll("\\p{Punct}", "").split(" ");
                List<String> wordList = new ArrayList<>();
                for (String word:words) {
                    wordList.add(word);
                }
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
    }

    public List<T> search(String term) {
        if (!this.index.containsKey(term.toLowerCase())) {
            return null;
        }

        //Sort the Amazon objects that term corresponding to, base on the term frequency, and return the sorted Amazon list
        List<Map.Entry<Integer, Integer>> entries = this.index.get(term.toLowerCase()).stream().sorted(Map.Entry.comparingByValue()).toList();
        List<T> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : entries) {
            result.add(documentID.get(entry.getKey()));
        }
        return result;
    }

}

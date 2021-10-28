package cs601.project3;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read file and build "asin-Amazon object list" HashMap
 */
public class ReadFile {

    private static final Logger LOGGER = LogManager.getLogger(ReadFile.class.getName());

    private final String path;

    //file path
    public ReadFile(String path) {
        this.path = path;
    }

    /**
     * Read file and convert each line to AmazonObject then store in the HashMap
     * @param amazonClass a class that extends from Amazon
     * @return a hashmap that contains all asin and their corresponding Amazon object
     */
    public Map<String, List<Amazon>> readFile(Class<? extends Amazon> amazonClass) {

        Map<String, List<Amazon>> asinMap = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), StandardCharsets.ISO_8859_1)) {
            String line = "";
            while ((line = br.readLine()) != null) {
                Gson gson = new Gson();
                Amazon amazonObject;
                try {
                    amazonObject = gson.fromJson(line, amazonClass);
                } catch (RuntimeException e) {
                    LOGGER.error("Rumtime exception.", e);
                    continue;
                }
                if (amazonObject == null) {
                    continue;
                }
                if (amazonObject.getAsin() == null) {
                    continue;
                }
                if (!asinMap.containsKey(amazonObject.getAsin().toUpperCase())) {
                    asinMap.put(amazonObject.getAsin().toUpperCase(), new ArrayList<>());
                }
                asinMap.get(amazonObject.getAsin()).add(amazonObject);

            }
        } catch (IOException e) {
            LOGGER.error("File " + path + " can't be found!", e);

        }
        return asinMap;
    }
}

package cs601.project3;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {
    private final String arg;

    public ReadFile(String arg) {
        this.arg = arg;
    }

    public Map<String, List<Amazon>> readFile(Class<? extends Amazon> amazonClass) {

        Map<String, List<Amazon>> asinMap = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(arg), StandardCharsets.ISO_8859_1)) {
            String line = "";
            while ((line = br.readLine()) != null) {
                Gson gson = new Gson();
                Amazon amazonObject;
                try {
                    amazonObject = gson.fromJson(line, amazonClass);
                } catch (RuntimeException e) {
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
            System.out.println("File " + arg + " can't be found!");
            System.exit(1);
        }
        return asinMap;
    }
}

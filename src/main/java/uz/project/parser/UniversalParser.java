package uz.project.parser;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;


// Universal parser to Map configuration file(settings.yaml)
public class UniversalParser {
    private String inputFilePath;
    private String filterRequest;

    public UniversalParser(String configFilePath) {
        parseConfig(configFilePath);
    }

    // Assigning config file fields to strings
    private void parseConfig(String configFilePath) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFilePath)) {
            Map<String, Object> config = yaml.load(inputStream);
            if (config != null) {
                this.inputFilePath = config.get("input_file").toString();
                this.filterRequest = config.get("filter_request").toString();
            } else {
                throw new RuntimeException("Configuration file is empty or incorrectly formatted.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load or parse the configuration file: " + configFilePath, e);
        }
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getFilterRequest() {
        return filterRequest;
    }
}
package co.edu.uptc.config;

import java.io.InputStream;
import java.util.Properties;

public class GlobalConfig {
    private final Properties props = new Properties();

    public void load(String fileName) {
        try (InputStream input = getInputStream(fileName)) {
            loadFromStream(input);
        } catch (Exception e) {
            handleLoadError(e);
        }
    }

    public String getConfig(String key) {
        return props.getProperty(key);
    }

    private InputStream getInputStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    private void loadFromStream(InputStream input) throws Exception {
        if (input != null) {
            props.load(input);
        }
    }

    private void handleLoadError(Exception e) {
        System.err.println("Error loading config: " + e.getMessage());
    }
}

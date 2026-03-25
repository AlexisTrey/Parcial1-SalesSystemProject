package co.edu.uptc.config;

import java.io.FileInputStream;
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

    private InputStream getInputStream(String fileName) throws Exception {
        InputStream external = tryExternalFile(fileName);
        if (external != null) {
            return external;
        }
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    private InputStream tryExternalFile(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (Exception e) {
            return null;
        }
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

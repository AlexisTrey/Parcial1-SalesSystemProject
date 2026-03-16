package co.edu.uptc.config;

public class AppConfig {
    private static AppConfig instance;
    private final GlobalConfig config = new GlobalConfig();

    private AppConfig() {
        config.load("config/config.properties");
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String get(String key) {
        return config.getConfig(key);
    }

    public int getInt(String key, int defaultValue) {
        return parseIntOrDefault(config.getConfig(key), defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        return parseDoubleOrDefault(config.getConfig(key), defaultValue);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

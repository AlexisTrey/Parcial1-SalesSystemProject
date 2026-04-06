package co.edu.uptc.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger("uptc.app");
    private static final String DEFAULT_PATH = "data/logs/uptc.log";
    private static final I18n i18n = I18n.getInstance();

    static {
        try {
            String path = resolveLogPath();
            ensureDirectory(path);
            FileHandler handler = new FileHandler(path, true);
            handler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(handler);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {
            LOGGER.warning(i18n.get("log.file.init.error") + ": " + e.getMessage());
        }
    }

    private AppLogger() {}

    public static void error(Class<?> origin, String msg, Throwable cause) {
        LOGGER.log(Level.SEVERE, "[" + origin.getSimpleName() + "] " + msg, cause);
    }

    public static void warn(Class<?> origin, String msg) {
        LOGGER.log(Level.WARNING, "[" + origin.getSimpleName() + "] " + msg);
    }

    public static void info(Class<?> origin, String msg) {
        LOGGER.log(Level.INFO, "[" + origin.getSimpleName() + "] " + msg);
    }

    private static String resolveLogPath() {
        String path = AppConfig.getInstance().get("data.logs.path");
        return (path == null || path.isBlank()) ? DEFAULT_PATH : path;
    }

    private static void ensureDirectory(String filePath) throws IOException {
        java.nio.file.Path parent = java.nio.file.Paths.get(filePath).getParent();
        if (parent != null && !java.nio.file.Files.exists(parent)) {
            java.nio.file.Files.createDirectories(parent);
        }
    }
}

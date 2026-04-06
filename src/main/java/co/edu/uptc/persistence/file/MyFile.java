package co.edu.uptc.persistence.file;

import co.edu.uptc.config.AppLogger;
import co.edu.uptc.config.I18n;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class MyFile {
    private static final I18n i18n = I18n.getInstance();

    public List<String> readLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            AppLogger.warn(MyFile.class, i18n.get("log.file.read.error") + ": " + filePath + " — " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void writeLines(String filePath, List<String> lines) {
        try {
            ensureDirectoryExists(filePath);
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            AppLogger.error(MyFile.class, i18n.get("log.file.write.error") + ": " + filePath, e);
        }
    }

    public void appendLine(String filePath, String line) {
        try {
            ensureDirectoryExists(filePath);
            Files.write(Paths.get(filePath),
                    (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            AppLogger.error(MyFile.class, i18n.get("log.file.append.error") + ": " + filePath, e);
        }
    }

    private void ensureDirectoryExists(String filePath) throws IOException {
        Path parent = Paths.get(filePath).getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}

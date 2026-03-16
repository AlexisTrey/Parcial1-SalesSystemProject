package co.edu.uptc.persistence.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyFile {
    public List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            handleReadError(e);
        }
        return lines;
    }

    public void writeLines(String filePath, List<String> lines) {
        try {
            ensureDirectoryExists(filePath);
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            handleWriteError(e);
        }
    }

    private void ensureDirectoryExists(String filePath) throws IOException {
        Path parent = Paths.get(filePath).getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private void handleReadError(IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
    }

    private void handleWriteError(IOException e) {
        System.err.println("Error writing file: " + e.getMessage());
    }
}

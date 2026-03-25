package co.edu.uptc.util;

import java.util.Set;

public class TablePrinter {
    private static final int MIN_COL_WIDTH = 18;

    public static void print(String[] headers, String[][] rows) {
        print(headers, rows, false);
    }

    public static void print(String[] headers, String[][] rows, boolean allLeftAlign) {
        if (rows == null || rows.length == 0) {
            System.out.println("  (sin datos)");
            return;
        }
        int[] widths = calculateWidths(headers, rows);
        String border = buildBorder(widths);
        printHeader(headers, widths, border);
        for (String[] row : rows) {
            printRow(row, widths, allLeftAlign);
        }
        System.out.println(border);
    }

    private static void printHeader(String[] headers, int[] widths, String border) {
        System.out.println(border);
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < headers.length; i++) {
            sb.append(formatCell(headers[i], widths[i], false));
        }
        System.out.println(sb);
        System.out.println(border);
    }

    private static void printRow(String[] cells, int[] widths, boolean allLeftAlign) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            String cell = (i < cells.length && cells[i] != null) ? cells[i] : "";
            boolean rightAlign = !allLeftAlign && isNumericLike(cell);
            sb.append(formatCell(cell, widths[i], rightAlign));
        }
        System.out.println(sb);
    }

    private static String formatCell(String text, int width, boolean rightAlign) {
        int inner = width - 2;
        String truncated = truncate(text, inner);
        if (rightAlign) {
            return String.format(" %" + inner + "s |", truncated);
        } else {
            return String.format(" %-" + inner + "s |", truncated);
        }
    }

    private static boolean isNumericLike(String text) {
        if (text == null || text.isBlank()) return false;
        return text.matches("-?\\d+([.,]\\d+)?");
    }

    private static String truncate(String text, int max) {
        if (text == null) return "";
        if (text.length() > max) return text.substring(0, max - 2) + "..";
        return text;
    }

    private static int[] calculateWidths(String[] headers, String[][] rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = Math.max(MIN_COL_WIDTH, headers[i].length() + 4);
        }
        for (String[] row : rows) {
            for (int i = 0; i < Math.min(row.length, widths.length); i++) {
                if (row[i] != null) {
                    widths[i] = Math.max(widths[i], row[i].length() + 4);
                }
            }
        }
        return widths;
    }

    private static String buildBorder(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            sb.append("-".repeat(w)).append("+");
        }
        return sb.toString();
    }
}

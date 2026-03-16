package co.edu.uptc.util;

public class TablePrinter {
    private static final int COL_WIDTH = 18;

    public static void print(String[] headers, String[][] rows) {
        int[] widths = calculateWidths(headers, rows);
        String border = buildBorder(widths);
        printTable(headers, rows, widths, border);
    }

    private static void printTable(String[] headers, String[][] rows, int[] widths, String border) {
        System.out.println(border);
        printRow(headers, widths);
        System.out.println(border);
        printAllRows(rows, widths);
        System.out.println(border);
    }

    private static void printAllRows(String[][] rows, int[] widths) {
        for (String[] row : rows) {
            printRow(row, widths);
        }
    }

    private static void printRow(String[] cells, int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < cells.length; i++) {
            sb.append(formatCell(cells[i], widths[i]));
        }
        System.out.println(sb);
    }

    private static String formatCell(String text, int width) {
        String truncated = truncate(text, width - 2);
        return String.format(" %-" + (width - 2) + "s |", truncated);
    }

    private static String truncate(String text, int max) {
        if (text == null)
            return "";
        if (text.length() > max)
            return text.substring(0, max - 2) + "..";
        return text;
    }

    private static int[] calculateWidths(String[] headers, String[][] rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = Math.max(COL_WIDTH, headers[i].length() + 4);
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

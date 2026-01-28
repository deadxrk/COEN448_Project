package ca.concordia.coen448;

public class Floor {
    private final int size;
    private final int[][] grid;

    public Floor(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getCell(int r, int c) {
        return grid[r][c];
    }

    public void mark(int r, int c) {
        grid[r][c] = 1;
    }

    // Simple padding so single-digit numbers line up
    private String pad2(int x) {
        String s = String.valueOf(x);
        if (s.length() == 1) return " " + s;
        return s; // for 2+ digits
    }

    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();

        // column header
        sb.append("   ");
        for (int c = 0; c < size; c++) {
            sb.append(pad2(c));
        }
        sb.append("\n");

        // rows
        for (int r = 0; r < size; r++) {
            sb.append(pad2(r)).append(" ");
            for (int c = 0; c < size; c++) {
                sb.append(grid[r][c] == 1 ? " *" : "  ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}

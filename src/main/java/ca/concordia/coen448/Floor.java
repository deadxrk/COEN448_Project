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

    public String toDisplayString() { //refactored using codex see gb2 prompt
        StringBuilder sb = new StringBuilder();

        // rows (mirror vertically so row 0 is at the bottom)
        for (int r = size - 1; r >= 0; r--) {
            sb.append(pad2(r)).append(" ");
            for (int c = 0; c < size; c++) {
                sb.append(grid[r][c] == 1 ? " *" : "  ");
            }
            sb.append("\n");
        }

        // column header at bottom
        sb.append("   ");
        for (int c = 0; c < size; c++) {
            sb.append(pad2(c));
        }
        sb.append("\n");

        return sb.toString();
    }
}

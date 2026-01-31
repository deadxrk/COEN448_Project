package ca.concordia.coen448;

import java.util.ArrayList;
import java.util.List;

public class RobotSimulator {
    // Coordinates follow the sample: [row, col]
    // Facing NORTH increases col; EAST increases row.
    private Floor floor;
    private int row;
    private int col;
    private boolean penDown;
    private Direction dir;
    private boolean quit;

    private final List<String> history = new ArrayList<>();

    public RobotSimulator() {
        init(20);
    }

    public void init(int n) {
        floor = new Floor(n);
        row = 0;
        col = 0;
        penDown = false;
        dir = Direction.NORTH;
        quit = false;
    }

    public boolean shouldQuit() {
        return quit;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isPenDown() { return penDown; }
    public Direction getDirection() { return dir; }
    public Floor getFloor() { return floor; }

    public String statusString() {
        return "Position: " + row + ", " + col +
                " - Pen: " + (penDown ? "down" : "up") +
                " - Facing: " + dir.name().toLowerCase() + "\n";
    }

    // Main entry for command lines coming from the user
    public String handleLine(String line) {
        if (line == null) return "";
        String trimmed = line.trim();
        if (trimmed.isEmpty()) return "";

        // Don't record H itself to avoid weird recursion
        String cmdLetter = trimmed.split("\\s+")[0].toUpperCase();
        if (!cmdLetter.equals("H")) {
            history.add(trimmed);
        }

        return execute(trimmed, true);
    }

    // If recordOutput=true, we return normal outputs for C/P/H
    // During replay we can still return output, but we keep it simple.
    private String execute(String line, boolean recordOutput) {
        String[] parts = line.trim().split("\\s+");
        String cmd = parts[0].toUpperCase();

        if (cmd.equals("Q")) {
            quit = true;
            return "";
        }

        if (cmd.equals("U")) {
            penDown = false;
            return "";
        }

        if (cmd.equals("D")) {
            penDown = true;
            return "";
        }

        if (cmd.equals("L")) {
            dir = dir.turnLeft();
            return "";
        }

        if (cmd.equals("R")) {
            dir = dir.turnRight();
            return "";
        }

        if (cmd.equals("C")) {
            return recordOutput ? statusString() : "";
        }

        if (cmd.equals("P")) {
            return recordOutput ? floor.toDisplayString() : "";
        }

        if (cmd.equals("I")) {
            if (parts.length != 2) return "Error: I needs a number, like: I 10\n";
            int n;
            try {
                n = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return "Error: invalid number for I\n";
            }
            if (n <= 0) return "Error: n must be > 0\n";
            init(n);
            return "";
        }

        if (cmd.equals("M")) {
            if (parts.length != 2) return "Error: M needs steps, like: M 4\n";
            int steps;
            try {
                steps = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return "Error: invalid number for M\n";
            }
            if (steps < 0) return "Error: steps must be >= 0\n";
            move(steps);
            return "";
        }

        if (cmd.equals("H")) {
            return recordOutput ? replayHistory() : "";
        }

        return "Error: unknown command\n";
    }

    private void move(int steps) {
        // If pen is down, mark starting cell too
        if (penDown) {
            floor.mark(row, col);
        }

        for (int i = 0; i < steps; i++) {
            int nextRow = row;
            int nextCol = col;

            switch (dir) {
                case NORTH: nextRow++; break; // matches example
                case EAST:  nextCol++; break;
                case SOUTH: nextRow--; break;
                case WEST:  nextCol--; break;
                default: break;
            }

            // Boundary rule: stop at edge (ignore remaining steps)
            if (nextRow < 0 || nextRow >= floor.getSize() ||
                nextCol < 0 || nextCol >= floor.getSize()) {
                break;
            }

            row = nextRow;
            col = nextCol;

            if (penDown) {
                floor.mark(row, col);
            }
        }
    }

    private String replayHistory() {
        // Replay from the beginning using a fresh simulator (same current size)
        RobotSimulator replay = new RobotSimulator();
        replay.init(this.floor.getSize());

        StringBuilder sb = new StringBuilder();
        sb.append("=== Replay start ===\n");

        for (String cmdLine : history) {
            // Show what is being replayed
            sb.append("> ").append(cmdLine).append("\n");
            replay.execute(cmdLine, false);
        }

        // Show end state and floor
        sb.append("--- Final state after replay ---\n");
        sb.append(replay.statusString());
        sb.append(replay.getFloor().toDisplayString());
        sb.append("=== Replay end ===\n");

        return sb.toString();
    }
}

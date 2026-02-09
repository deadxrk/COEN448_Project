package ca.concordia.coen448;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LetterHTest {

    @Test
    void drawH_andPrintFloor() {
        RobotSimulator sim = new RobotSimulator();

        // Make a known floor size
        sim.handleLine("I 10");

        // Draw left vertical bar (col 0, rows 0..5)
        sim.handleLine("D");     // pen down
        sim.handleLine("M 5");   // facing NORTH by default -> row increases

        // Move to top of right bar without drawing
        sim.handleLine("U");     // pen up
        sim.handleLine("R");     // face EAST (col increases)
        sim.handleLine("M 4");   // go to col 4 (still at row 5)

        // Draw right vertical bar down (col 4, rows 5..0)
        sim.handleLine("D");     // pen down
        sim.handleLine("R");     // from EAST -> SOUTH
        sim.handleLine("M 5");   // row decreases

        // Move to middle bar position (row 3, col 4) without drawing
        sim.handleLine("U");     // pen up
        sim.handleLine("L");     // SOUTH -> EAST
        sim.handleLine("L");     // EAST  -> NORTH
        sim.handleLine("M 3");   // go up to row 3

        // Draw middle bar left (row 3, cols 4..0)
        sim.handleLine("D");     // pen down
        sim.handleLine("L");     // NORTH -> WEST
        sim.handleLine("M 4");   // col decreases

        // ---- PRINT (TA wants to see the H) ----
        String printed = sim.handleLine("P");
        System.out.println("\n=== PRINTING H ===");
        System.out.println(printed);

        // ---- ASSERT end position & direction (optional but nice) ----
        assertEquals(3, sim.getRow(), "Robot row should end at 3");
        assertEquals(0, sim.getCol(), "Robot col should end at 0");
        assertEquals(Direction.WEST, sim.getDirection(), "Robot should face WEST");

        // ---- ASSERT the H shape on the grid ----
        Floor f = sim.getFloor();

        // Left vertical: col 0, rows 0..5
        for (int r = 0; r <= 5; r++) {
            assertEquals(1, f.getCell(r, 0), "Left bar missing at (" + r + ",0)");
        }

        // Right vertical: col 4, rows 0..5
        for (int r = 0; r <= 5; r++) {
            assertEquals(1, f.getCell(r, 4), "Right bar missing at (" + r + ",4)");
        }

        // Middle bar: row 3, cols 0..4
        for (int c = 0; c <= 4; c++) {
            assertEquals(1, f.getCell(3, c), "Middle bar missing at (3," + c + ")");
        }
    }
}

package ca.concordia.coen448;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// QA Coverage Tests for Task 3

public class QACoverageTest {

    private RobotSimulator robot;

    @BeforeEach
    void setUp() {
        robot = new RobotSimulator();
        robot.init(10); // 10x10 grid
    }

    // A. STATEMENT COVERAGE

    @Test
    void testStatementCoverage_move() {
        // Pen down: mark start and each step
        robot.handleLine("D");
        robot.handleLine("M 3"); // move north 3 steps
        // Starting cell (0,0) and cells (1,0), (2,0), (3,0) should be 1
        assertEquals(1, robot.getFloor().getCell(0, 0));
        assertEquals(1, robot.getFloor().getCell(1, 0));
        assertEquals(1, robot.getFloor().getCell(2, 0));
        assertEquals(1, robot.getFloor().getCell(3, 0));
        assertEquals(0, robot.getFloor().getCell(4, 0));

        // Pen up: move, no marking
        robot.handleLine("U");
        robot.handleLine("M 2"); // continues north
        assertEquals(5, robot.getRow()); // row becomes 5
        assertEquals(0, robot.getCol());
        // No new marks
        assertEquals(0, robot.getFloor().getCell(4, 0));
        assertEquals(0, robot.getFloor().getCell(5, 0));

        // Move zero steps – no change
        robot.handleLine("M 0");
        assertEquals(5, robot.getRow());
    }

    @Test
    void testStatementCoverage_moveDetailed() {
        // Test 1: pen down, move north
        robot.init(5);
        robot.handleLine("D");
        robot.handleLine("M 3");
        assertEquals(3, robot.getRow());  // row increased
        assertEquals(0, robot.getCol());
        // Check marks: start + three steps = 4 cells marked
        for (int i = 0; i <= 3; i++) {
            assertEquals(1, robot.getFloor().getCell(i, 0));
        }

        // Test 2: pen up, move east
        robot.init(5);
        robot.handleLine("R"); // turn east
        robot.handleLine("U");
        robot.handleLine("M 2");
        assertEquals(0, robot.getRow());
        assertEquals(2, robot.getCol());
        // No marks
        assertEquals(0, robot.getFloor().getCell(0, 0));
        assertEquals(0, robot.getFloor().getCell(0, 1));

        // Test 3: move zero steps
        robot.init(5);
        robot.handleLine("M 0");
        assertEquals(0, robot.getRow());
        assertEquals(0, robot.getCol());

        // Test 4: move out of bounds (stop at edge)
        robot.init(5);
        robot.handleLine("D");
        robot.handleLine("M 10"); // only 5 rows
        assertEquals(4, robot.getRow()); // max row index is 4
        assertEquals(0, robot.getCol());
        // Only rows 0..4 should be marked
        for (int i = 0; i < 5; i++) {
            assertEquals(1, robot.getFloor().getCell(i, 0));
        }
    }

    @Test
    void testStatementCoverage_floorMark() {
        Floor floor = robot.getFloor();
        floor.mark(2, 3);
        assertEquals(1, floor.getCell(2, 3));
        floor.mark(2, 3); // same cell – stays 1
        assertEquals(1, floor.getCell(2, 3));
        assertThrows(IllegalArgumentException.class, () -> floor.mark(-1, 0));
    }

    @Test
    void testStatementCoverage_commands() {
        robot.init(5);
        robot.handleLine("U");
        robot.handleLine("D");
        robot.handleLine("L");
        robot.handleLine("R");
        robot.handleLine("C");
        robot.handleLine("P");
        robot.handleLine("I 7");
        assertEquals(7, robot.getFloor().getSize());
        // Invalid I
        String out = robot.handleLine("I abc");
        assertTrue(out.contains("Error"));
        out = robot.handleLine("I -5");
        assertTrue(out.contains("Error"));
        // Invalid M
        out = robot.handleLine("M x");
        assertTrue(out.contains("Error"));
        out = robot.handleLine("M -1");
        assertTrue(out.contains("Error"));
        // Unknown command
        out = robot.handleLine("X");
        assertTrue(out.contains("unknown"));
        // Q
        robot.handleLine("Q");
        assertTrue(robot.shouldQuit());
        // Reset
        robot.init(5);
    }

    // B. DECISION COVERAGE (Branch Coverage)

    @Test
    void testDecisionCoverage_penState() {
        // True branch: pen down
        robot.init(5);
        robot.handleLine("D");
        robot.handleLine("M 2");
        // Starting cell + two steps = three marks
        assertEquals(1, robot.getFloor().getCell(0, 0));
        assertEquals(1, robot.getFloor().getCell(1, 0));
        assertEquals(1, robot.getFloor().getCell(2, 0));

        // False branch: pen up
        robot.init(5);
        robot.handleLine("U");
        robot.handleLine("M 2");
        assertEquals(0, robot.getFloor().getCell(0, 0));
        assertEquals(0, robot.getFloor().getCell(1, 0));
    }

    @Test
    void testDecisionCoverage_boundary() {
        // True branch: out of bounds -> break
        robot.init(3);
        robot.handleLine("M 5"); // moves north, should stop at row 2
        assertEquals(2, robot.getRow());
        assertEquals(0, robot.getCol());

        // False branch: within bounds
        robot.init(3);
        robot.handleLine("M 2");
        assertEquals(2, robot.getRow());
    }

    @Test
    void testDecisionCoverage_handleLine() {
        robot.init(5);
        robot.handleLine("U");
        assertFalse(robot.isPenDown());
        robot.handleLine("D");
        assertTrue(robot.isPenDown());
        robot.handleLine("L");
        assertEquals(Direction.WEST, robot.getDirection());
        robot.handleLine("R");
        assertEquals(Direction.NORTH, robot.getDirection());
        robot.handleLine("C");
        robot.handleLine("P");
        robot.handleLine("I 8");
        assertEquals(8, robot.getFloor().getSize());
        robot.handleLine("M 3");
        assertEquals(3, robot.getRow());
        assertEquals(0, robot.getCol());
        robot.handleLine("Q");
        assertTrue(robot.shouldQuit());
        robot.init(5);
    }

    // C. CONDITION COVERAGE (boundary checks in move())

    @Test
    void testConditionCoverage_boundaryConditions() {
        // Each atomic condition in the OR expression:
        //   if (nextRow < 0 || nextRow >= size || nextCol < 0 || nextCol >= size)

        robot.init(5);

        // 1. nextRow < 0  true (move south from row 0)
        robot.handleLine("R"); // turn east
        robot.handleLine("R"); // now south
        robot.handleLine("M 1");
        assertEquals(0, robot.getRow()); // no movement
        assertEquals(0, robot.getCol());

        // 2. nextRow >= size true (move north from top row)
        robot.init(5);
        robot.handleLine("M 4"); // row 4
        robot.handleLine("M 1");
        assertEquals(4, robot.getRow()); // stopped at edge

        // 3. nextCol < 0 true (move west from col 0)
        robot.init(5);
        robot.handleLine("L"); // west
        robot.handleLine("M 1");
        assertEquals(0, robot.getCol());

        // 4. nextCol >= size true (move east from rightmost col)
        robot.init(5);
        robot.handleLine("R"); // east
        robot.handleLine("M 4"); // col 4
        robot.handleLine("M 1");
        assertEquals(4, robot.getCol());

        // False branches: covered in previous tests (moves that stay inside)
    }

    // D. MULTIPLE CONDITION COVERAGE (boundary check)
    //    Since conditions are mutually exclusive in a single move,
    //    testing each true and all false gives >50% coverage.

    @Test
    void testMultipleConditionCoverage_boundaryCombinations() {
        robot.init(5);

        // All false – move within bounds
        robot.handleLine("M 2");
        assertEquals(2, robot.getRow());
        assertEquals(0, robot.getCol());

        // nextRow < 0 true, others false
        robot.init(5);
        robot.handleLine("R"); robot.handleLine("R"); // south
        robot.handleLine("M 1");
        assertEquals(0, robot.getRow());

        // nextRow >= size true, others false
        robot.init(5);
        robot.handleLine("M 4");
        robot.handleLine("M 1");
        assertEquals(4, robot.getRow());

        // nextCol < 0 true, others false
        robot.init(5);
        robot.handleLine("L"); // west
        robot.handleLine("M 1");
        assertEquals(0, robot.getCol());

        // nextCol >= size true, others false
        robot.init(5);
        robot.handleLine("R"); // east
        robot.handleLine("M 4");
        robot.handleLine("M 1");
        assertEquals(4, robot.getCol());
    }

    // Additional coverage for History command
    @Test
    void testHistoryReplayCoverage() {
        robot.init(5);
        robot.handleLine("D");
        robot.handleLine("M 2");
        robot.handleLine("R");
        robot.handleLine("M 1");
        String replay = robot.handleLine("H");
        assertNotNull(replay);
        assertTrue(replay.contains("Replay start"));
    }

    @Test
    void testDirectionCoverage() {
        // Test turnLeft from each direction (covers all 4 branches)
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());

        // Test turnRight from each direction (covers all 4 branches)
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }
}


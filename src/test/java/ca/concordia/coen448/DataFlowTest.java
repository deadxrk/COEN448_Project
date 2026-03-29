package ca.concordia.coen448;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataFlowTest {

    private RobotSimulator robot;

    @BeforeEach
    void setUp() {
        robot = new RobotSimulator();
        robot.init(10); // 10x10 grid
    }

    //DF1: steps < 0 (p-use)
    @Test
    void testNegativeSteps(){

        String output = robot.handleLine("M -1");
        assertTrue(output.contains("Error: steps must be >= 0"));

    }

    //DF2: move(steps) (c-use)
    @Test
    void testValidSteps(){
        robot.handleLine("D");
        robot.handleLine("M 2");
        assertEquals(2, robot.getRow());
        assertEquals(0, robot.getCol());
    }
    
    //DF3: n <= 0 (p-use)
    @Test
    void testInvalidInitSize(){
        String output = robot.handleLine("I -5");
        assertTrue(output.contains("Error: n must be > 0"));
    }

    //DF4: init(n) (c-use)
    @Test
    void testValidInitSize(){
        robot.handleLine("I 12");
        assertEquals(12, robot.getFloor().getSize());
    }

    //DF5: quit defined (c-use)
    @Test
    void testQuitFlag(){
        robot.handleLine("Q");
        assertTrue(robot.shouldQuit());
    }

    //DF6: branching on command (p-use)
    @Test
    void testCommandParsing(){
        String output = robot.handleLine("C");
        assertNotNull(output);
        assertTrue(output.contains("Position"));
    }

    //DF7: display floor (p-use)
    @Test
    void testParsingPrint(){
        String output = robot.handleLine("P");
        assertNotNull(output);
        assertTrue(output.contains("*") || output.contains("0")); // floor should show something
    }

}

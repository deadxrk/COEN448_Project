package ca.concordia.coen448;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RSTest {
    
    @Test
    void TestRS_turnLeft(){
        RobotSimulator RS=new RobotSimulator();
        Direction leftTurnArr[]={
            Direction.WEST,Direction.SOUTH,
            Direction.EAST,Direction.NORTH};
        //initial direction is assumed to be NORTH
        for(Direction cur:leftTurnArr){
            RS.handleLine("L");
            assertEquals(cur, RS.getDirection());
        }
        //sloppy, but test for both symbols
        for(Direction cur:leftTurnArr){
            RS.handleLine("l");
            assertEquals(cur, RS.getDirection());
        }
        return;
    }

    @Test
    void TestRS_turnRight(){
        RobotSimulator RS=new RobotSimulator();
        Direction rightTurnArr[]={
            Direction.EAST,Direction.SOUTH,
            Direction.WEST,Direction.NORTH};
        //initial direction is assumed to be NORTH
        for(Direction cur:rightTurnArr){
            RS.handleLine("R");
            assertEquals(cur, RS.getDirection());
        }
        //sloppy, but test for both symbols
        for(Direction cur:rightTurnArr){
            RS.handleLine("r");
            assertEquals(cur, RS.getDirection());;
        }
        return;
    }

    @Test
    void TestRS_PenState(){
        RobotSimulator RS=new RobotSimulator();
        assertFalse(RS.isPenDown());
        RS.handleLine("D");
        assert(RS.isPenDown()==true);
        RS.handleLine("U");
        assertFalse(RS.isPenDown());
        RS.handleLine("d");
        assert(RS.isPenDown()==true);
        RS.handleLine("u");
        assertFalse(RS.isPenDown());
    }

    @Test
    void TestRS_Move(){

        RobotSimulator RS=new RobotSimulator();
        
        String cmd[]={"M 19","R","M 10","R","M 15","R",
        "m 4"};
        int exp_row[]={19,19,19,19,4,4,4};
        int exp_col[]={0,0,10,10,10,10,6};

        for(int i=0; i<cmd.length;++i){
            RS.handleLine(cmd[i]);
            assert(RS.getRow()==exp_row[i]
            && RS.getCol()==exp_col[i]);
        }
    }

    @Test
    void TestRS_SmallStepCountsExact() {
        RobotSimulator RS = new RobotSimulator();

        RS.handleLine("D");
        RS.handleLine("M 1");
        assert(RS.getRow()==1);
        assert(RS.getCol()==0);
        assert(RS.getFloor().getCell(0, 0)==1);
        assert(RS.getFloor().getCell(1, 0)==1);
        assert(RS.getFloor().getCell(2, 0)==0);

        RS.handleLine("R");
        RS.handleLine("M 2");
        assert(RS.getRow()==1);
        assert(RS.getCol()==2);
        assert(RS.getFloor().getCell(1, 1)==1);
        assert(RS.getFloor().getCell(1, 2)==1);
        assert(RS.getFloor().getCell(1, 3)==0);
    }

    @Test
    void TestRS_EdgeStopsAtBoundary() {
        RobotSimulator RS = new RobotSimulator();

        RS.handleLine("D");
        RS.handleLine("M 21");
        assert(RS.getRow()==19);
        assert(RS.getCol()==0);
        for (int r = 0; r < 20; r++) {
            assert(RS.getFloor().getCell(r, 0)==1);
        }

        RS.handleLine("R");
        RS.handleLine("M 21");
        assert(RS.getRow()==19);
        assert(RS.getCol()==19);
        for (int c = 0; c < 20; c++) {
            assert(RS.getFloor().getCell(19, c)==1);
        }
    }

    @Test
    void TestRS_Edge(){
        RobotSimulator RS=new RobotSimulator();
        
        String cmd[]={"M 21","R","M 21","R","M 21","R",
        "m 21"};
        int exp_row[]={19,19,19,19,0,0,0};
        int exp_col[]={0,0,19,19,19,19,0};

        for(int i=0; i<cmd.length;++i){
            RS.handleLine(cmd[i]);
            assert(RS.getRow()==exp_row[i]
            && RS.getCol()==exp_col[i]);
        }
    }

    @Test
    void TestRS_init(){
        RobotSimulator RS=new RobotSimulator();

        RS.handleLine("I 10");

        Floor floor = RS.getFloor();

        assert(floor.getSize()==10);
    }

    @Test
    void TestRS_Quit(){
        RobotSimulator RS = new RobotSimulator();

        assert(RS.shouldQuit() == false);

        String output = RS.handleLine("Q");

        assert(RS.shouldQuit() == true);
        assert(output.equals(""));
    }

    @Test
    void TestRS_FailureCases(){
        RobotSimulator RS = new RobotSimulator();

        assert(RS.handleLine("X").equals("Error: unknown command\n"));
        assert(RS.handleLine("I").equals("Error: I needs a number, like: I 10\n"));
        assert(RS.handleLine("I abc").equals("Error: invalid number for I\n"));
        assert(RS.handleLine("I 0").equals("Error: n must be > 0\n"));
        assert(RS.handleLine("M").equals("Error: M needs steps, like: M 4\n"));
        assert(RS.handleLine("M abc").equals("Error: invalid number for M\n"));
        assert(RS.handleLine("M -1").equals("Error: steps must be >= 0\n"));
    }

    @Test
    void TestRS_marktest(){ //refactored with context: prompt GB1
        RobotSimulator RS=new RobotSimulator();

        RS.handleLine("D");

        RS.handleLine("M 4");

        int[] cols={0,1,2,3,4};
        int[] rows={0,1,2,3,4};

        for(int r:rows){
            assert(RS.getFloor().getCell(r,0)==1);
        }

        RS.handleLine("R");
        RS.handleLine("M 4");

        for(int c:cols){
            assert(RS.getFloor().getCell(4,c)==1);
        }

        RS.handleLine("U");

        RS.handleLine("R");
        RS.handleLine("M 4");

        for(int r=0;r<4;++r){
            assert(RS.getFloor().getCell(r,4)==0);
        }
        assert(RS.getFloor().getCell(4,4)==1);

        RS.handleLine("R");
        RS.handleLine("M 4");

        for(int c=1;c<=4;++c){
            assert(RS.getFloor().getCell(0,c)==0);
        }
        assert(RS.getFloor().getCell(0,0)==1);

        // retrace marked vertical line with pen up
        RS.handleLine("R");
        RS.handleLine("M 4");
        for(int r:rows){
            assert(RS.getFloor().getCell(r,0)==1);
        }

        // retrace marked horizontal line with pen down
        RS.handleLine("D");
        RS.handleLine("R");
        RS.handleLine("M 4");
        for(int c:cols){
            assert(RS.getFloor().getCell(4,c)==1);
        }
    }

    @Test
    void TestRS_ExactMarkingPattern() {
        RobotSimulator RS = new RobotSimulator();

        RS.handleLine("D");
        RS.handleLine("M 4");
        RS.handleLine("R");
        RS.handleLine("M 4");
        RS.handleLine("R");
        RS.handleLine("M 4");
        RS.handleLine("R");
        RS.handleLine("M 4");

        int[][] exp = {
            {1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1}
        };

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                assertEquals(exp[r][c], RS.getFloor().getCell(r, c),
                        String.format("Cell does not match at (%d,%d)", r, c));
            }
        }
    }

    @Test
    void TestRS_PenUpDoesNotMarkNewPath() {
        RobotSimulator RS = new RobotSimulator();

        RS.handleLine("D");
        RS.handleLine("M 2"); 
        RS.handleLine("R");
        RS.handleLine("M 2"); 
        RS.handleLine("U");  
        RS.handleLine("L");  
        RS.handleLine("M 2"); 

        assert(RS.getFloor().getCell(3, 2)==0);
        assert(RS.getFloor().getCell(4, 2)==0);
    }

    @Test
void TestRS_InitResetsEverything() {
    RobotSimulator rs = new RobotSimulator();

    rs.handleLine("D");
    rs.handleLine("M 3");
    rs.handleLine("R");
    rs.handleLine("M 2");

    assertEquals(3, rs.getRow());
    assertEquals(2, rs.getCol());
    assertTrue(rs.isPenDown());

    rs.handleLine("I 5");

    assertEquals(5, rs.getFloor().getSize());
    assertEquals(0, rs.getRow());
    assertEquals(0, rs.getCol());
    assertFalse(rs.isPenDown());
    assertEquals(Direction.NORTH, rs.getDirection());
    assertFalse(rs.shouldQuit());

    for (int r = 0; r < 5; r++) {
        for (int c = 0; c < 5; c++) {
            assertEquals(0, rs.getFloor().getCell(r, c),
                    "Floor should be cleared after initialization");
        }
    }
}

@Test
void TestRS_HistoryReplayIsStable() {
    RobotSimulator rs = new RobotSimulator();

    rs.handleLine("I 5");
    rs.handleLine("D");
    rs.handleLine("M 2");
    rs.handleLine("R");
    rs.handleLine("M 1");

    String firstReplay = rs.handleLine("H");
    String secondReplay = rs.handleLine("H");

    assertEquals(firstReplay, secondReplay);
    assertTrue(firstReplay.contains("=== Replay start ==="));
    assertTrue(firstReplay.contains("> I 5"));
    assertTrue(firstReplay.contains("> D"));
    assertTrue(firstReplay.contains("> M 2"));
    assertTrue(firstReplay.contains("> R"));
    assertTrue(firstReplay.contains("> M 1"));
    assertTrue(firstReplay.contains("=== Replay end ==="));
}

@Test
void TestRS_HistoryAfterReinitialize() {
    RobotSimulator rs = new RobotSimulator();

    rs.handleLine("I 5");
    rs.handleLine("D");
    rs.handleLine("M 2");
    rs.handleLine("I 3");
    rs.handleLine("D");
    rs.handleLine("M 1");

    String replay = rs.handleLine("H");

    assertTrue(replay.contains("> I 5"));
    assertTrue(replay.contains("> I 3"));
    assertTrue(replay.contains("Position: 1, 0 - Pen: down - Facing: north"));
    assertTrue(replay.contains("=== Replay end ==="));

    // Verify current simulator state after reinitialize path
    assertEquals(3, rs.getFloor().getSize());
    assertEquals(1, rs.getRow());
    assertEquals(0, rs.getCol());
    assertTrue(rs.isPenDown());
    assertEquals(Direction.NORTH, rs.getDirection());

    assertEquals(1, rs.getFloor().getCell(0, 0));
    assertEquals(1, rs.getFloor().getCell(1, 0));
    assertEquals(0, rs.getFloor().getCell(2, 0));
}

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        quoteCharacter = '\'',
        value = {
            "'I 5;P'|' 4           \\n 3           \\n 2           \\n 1           \\n 0           \\n    0 1 2 3 4\\n'",
            "'I 5;D;M 2;P'|' 4           \\n 3           \\n 2  *        \\n 1  *        \\n 0  *        \\n    0 1 2 3 4\\n'",
            "'I 5;D;M 2;R;M 1;P'|' 4           \\n 3           \\n 2  * *      \\n 1  *        \\n 0  *        \\n    0 1 2 3 4\\n'"
        }
    )
    void TestRS_DisplayPrint(String commandSeries, String expectedDisplay){
        RobotSimulator RS = new RobotSimulator();

        String[] commands = commandSeries.split(";");
        String actualDisplay = "";

        for(String command : commands){
            actualDisplay = RS.handleLine(command);
        }

        assertEquals(expectedDisplay.replace("\\n", "\n"), actualDisplay);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        quoteCharacter = '\'',
        value = {
            "'C'|'Position: 0, 0 - Pen: up - Facing: north\\n'",
            "'D;M 2;C'|'Position: 2, 0 - Pen: down - Facing: north\\n'",
            "'R;M 3;C'|'Position: 0, 3 - Pen: up - Facing: east\\n'",
            "'D;R;M 2;L;M 1;C'|'Position: 1, 2 - Pen: down - Facing: north\\n'"
        }
    )
    void TestRS_StatusPrint(String commandSeries, String expectedStatus){
        RobotSimulator RS = new RobotSimulator();

        String[] commands = commandSeries.split(";");
        String actualStatus = "";

        for(String command : commands){
            actualStatus = RS.handleLine(command);
        }

        assertEquals(expectedStatus.replace("\\n", "\n"), actualStatus);
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = '|',
        quoteCharacter = '\'',
        value = {
            "'I 5;D;M 2'|'=== Replay start ===\\n> I 5\\n> D\\n> M 2\\n--- Final state after replay ---\\nPosition: 2, 0 - Pen: down - Facing: north\\n 4           \\n 3           \\n 2  *        \\n 1  *        \\n 0  *        \\n    0 1 2 3 4\\n=== Replay end ===\\n'",
            "'I 5;D;M 1;U;R;M 2'|'=== Replay start ===\\n> I 5\\n> D\\n> M 1\\n> U\\n> R\\n> M 2\\n--- Final state after replay ---\\nPosition: 1, 2 - Pen: up - Facing: east\\n 4           \\n 3           \\n 2           \\n 1  *        \\n 0  *        \\n    0 1 2 3 4\\n=== Replay end ===\\n'"
        }
    )
    void TestRS_HistoryPrint(String commandSeries, String expectedHistory){
        RobotSimulator RS = new RobotSimulator();

        String[] commands = commandSeries.split(";");

        for(String command : commands){
            RS.handleLine(command);
        }

        String actualHistory = RS.handleLine("H");

        assertEquals(expectedHistory.replace("\\n", "\n"), actualHistory);
}




}

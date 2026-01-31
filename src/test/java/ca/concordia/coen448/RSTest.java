package ca.concordia.coen448;

import org.junit.jupiter.api.Test;

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
            assert(RS.getDirection()==cur);
        }
        //sloppy, but test for both symbols
        for(Direction cur:leftTurnArr){
            RS.handleLine("l");
            assert(RS.getDirection()==cur);
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
            assert(RS.getDirection()==cur);
        }
        //sloppy, but test for both symbols
        for(Direction cur:rightTurnArr){
            RS.handleLine("r");
            assert(RS.getDirection()==cur);
        }
        return;
    }

    @Test
    void TestRS_PenState(){
        RobotSimulator RS=new RobotSimulator();
        assert(RS.isPenDown()==false);
        RS.handleLine("D");
        assert(RS.isPenDown()==true);
        RS.handleLine("U");
        assert(RS.isPenDown()==false);
        RS.handleLine("d");
        assert(RS.isPenDown()==true);
        RS.handleLine("u");
        assert(RS.isPenDown()==false);
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

}

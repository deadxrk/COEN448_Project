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

    @Test
    void TestRS_init(){
        RobotSimulator RS=new RobotSimulator();

        RS.handleLine("I 10");

        Floor floor = RS.getFloor();

        assert(floor.getSize()==10);
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

}

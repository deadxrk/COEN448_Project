package ca.concordia.coen448;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import java.util.Random;

// Simple tests for the floor class

public class FloorTest {
    @RepeatedTest(10)
    void sizeTest(){
        Random rand = new Random();
        int ranSize= rand.nextInt(10)+1;
        Floor floor = new Floor(ranSize);
        assert(floor.getSize() == ranSize);
    }

    @RepeatedTest(10)
    void markAndGetCellTest(){
        Random rand = new Random();
        int ranSize = rand.nextInt(10)+1;
        Floor floor = new Floor(ranSize);
        int ran_r = rand.nextInt(ranSize);
        int ran_c = rand.nextInt(ranSize);
        floor.mark(ran_r,ran_c);
        assert(floor.getCell(ran_r, ran_c) == 1);
    }

    //TODO:not sure how to test toDispayString
    //Maybe worth having an automated test for it
}

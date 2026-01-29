package ca.concordia.coen448;

import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        RobotSimulator sim = new RobotSimulator();
        Scanner sc = new Scanner(System.in);

        while(!sim.shouldQuit()){
            System.out.print("Enter Command : ");
            if(!sc.hasNextLine()){ break;}

            String line = sc.nextLine();
            String output = sim.handleLine(line);

            if(output!=null && !output.isBlank()){
                System.out.print(output);
                if(!output.endsWith("\n")) {System.out.print("\n");}
            }
            sc.close();
        }

        
    }
}

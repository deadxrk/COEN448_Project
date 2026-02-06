# UML Class Diagram - Robot Simulator

This diagram represents the class structure of the Robot Simulator application.
```mermaid
classDiagram
    class Main {
        +main(String[] args)$ void
    }

    class RobotSimulator {
        -Floor floor
        -int row
        -int col
        -boolean penDown
        -Direction dir
        -boolean quit
        -List~String~ history
        +RobotSimulator()
        +init(int n) void
        +shouldQuit() boolean
        +getRow() int
        +getCol() int
        +isPenDown() boolean
        +getDirection() Direction
        +getFloor() Floor
        +statusString() String
        +handleLine(String line) String
        -execute(String line, boolean recordOutput) String
        -move(int steps) void
        -replayHistory() String
    }

    class Floor {
        -int size
        -int[][] grid
        +Floor(int size)
        +getSize() int
        +getCell(int r, int c) int
        +mark(int r, int c) void
        +toDisplayString() String
        -pad2(int x) String
    }

    class Direction {
        <<enumeration>>
        NORTH
        EAST
        SOUTH
        WEST
        +turnLeft() Direction
        +turnRight() Direction
    }

    Main ..> RobotSimulator : uses
    RobotSimulator *-- Floor : contains
    RobotSimulator --> Direction : uses
```

## Class Descriptions

- **Main**: Entry point for the application
- **RobotSimulator**: Core simulator managing robot state and commands
- **Floor**: Represents the drawing surface grid
- **Direction**: Enum for cardinal directions with turn logic

stateDiagram-v2
    [*] --> Initialized : Constructor called

    Initialized --> PenUp : Default state
    
    state "Pen Up" as PenUp {
        [*] --> Idle
        Idle --> Moving : M s (move s steps)
        Moving --> Idle : Movement complete
        Idle --> Turning : L/R (turn)
        Turning --> Idle : Turn complete
        Idle --> CheckStatus : C (check status)
        CheckStatus --> Idle : Display status
        Idle --> PrintFloor : P (print floor)
        PrintFloor --> Idle : Display floor
        Idle --> Replay : H (replay history)
        Replay --> Idle : Replay complete
        Idle --> Reinitialize : I n (new floor)
        Reinitialize --> Idle : Floor created
    }
    
    state "Pen Down" as PenDown {
        [*] --> Idle
        Idle --> MovingAndMarking : M s (move & mark)
        MovingAndMarking --> Idle : Movement complete
        Idle --> Turning : L/R (turn)
        Turning --> Idle : Turn complete
        Idle --> CheckStatus : C (check status)
        CheckStatus --> Idle : Display status
        Idle --> PrintFloor : P (print floor)
        PrintFloor --> Idle : Display floor
        Idle --> Replay : H (replay history)
        Replay --> Idle : Replay complete
        Idle --> Reinitialize : I n (new floor)
        Reinitialize --> Idle : Floor created
    }
    
    PenUp --> PenDown : D (pen down)
    PenDown --> PenUp : U (pen up)
    
    PenUp --> [*] : Q (quit)
    PenDown --> [*] : Q (quit)
    
    note right of PenUp
        Pen is up
        Moving does NOT mark cells
        Position: (row, col)
        Direction: N/E/S/W
    end note
    
    note right of PenDown
        Pen is down
        Moving MARKS cells on grid
        Leaves trail of marks
    end note
    
    note left of MovingAndMarking
        Marks starting cell
        Marks each step
        Stops at boundaries
    end note
    

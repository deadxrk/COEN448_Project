stateDiagram-v2
    [*] --> Initialized : new RobotSimulator()

    Initialized --> PenUp : init() called
    
    PenUp --> PenDown : D command
    PenDown --> PenUp : U command
    
    PenUp --> Moving_NoMark : M s command
    Moving_NoMark --> PenUp : complete
    
    PenDown --> Moving_WithMark : M s command
    Moving_WithMark --> PenDown : complete
    
    PenUp --> Turning_Up : L or R command
    Turning_Up --> PenUp : complete
    
    PenDown --> Turning_Down : L or R command
    Turning_Down --> PenDown : complete
    
    PenUp --> DisplayStatus_Up : C command
    DisplayStatus_Up --> PenUp : complete
    
    PenDown --> DisplayStatus_Down : C command
    DisplayStatus_Down --> PenDown : complete
    
    PenUp --> PrintFloor_Up : P command
    PrintFloor_Up --> PenUp : complete
    
    PenDown --> PrintFloor_Down : P command
    PrintFloor_Down --> PenDown : complete
    
    PenUp --> Replaying_Up : H command
    Replaying_Up --> PenUp : complete
    
    PenDown --> Replaying_Down : H command
    Replaying_Down --> PenDown : complete
    
    PenUp --> Reinitializing_Up : I n command
    Reinitializing_Up --> PenUp : complete
    
    PenDown --> Reinitializing_Down : I n command
    Reinitializing_Down --> PenDown : complete
    
    PenUp --> [*] : Q command
    PenDown --> [*] : Q command

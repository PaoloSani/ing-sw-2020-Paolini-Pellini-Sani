package it.polimi.ingsw.GUI;

public enum Mode {
    NEW_GAME("A"),
    EXISTING_MATCH("B"),
    RANDOM_MATCH("C");

    private String mode;

    Mode(String mode){
        this.mode = mode;
    }

    @Override
    public String toString(){
        return mode;
    }
}

package it.polimi.ingsw.GUI;

public enum Mode {
    NEW_GAME("A"),
    EXISTING_MATCH("B"),
    RANDOM_MATCH("C"),
    DEFAULT("default");

    private String mode;

    Mode(String mode){
        this.mode = mode;
    }

    public static Mode fromText(String mode){
        for (Mode m : Mode.values()){
            if (m.mode.equals(mode)) return m;
        }
        return DEFAULT;
    }

    @Override
    public String toString(){
        return mode;
    }
}

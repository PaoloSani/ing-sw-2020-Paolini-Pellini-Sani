package it.polimi.ingsw.model;

import javafx.scene.image.Image;

public enum God {
    APOLLO("APOLLO","God Of Music\nYour Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated."),
    ARTEMIS("ARTEMIS",""),
    ATHENA("ATHENA",""),
    ATLAS("ATLAS",""),
    CHARON("CHARON",""),
    DEMETER("DEMETER",""),
    HEPHAESTUS("HEPHAESTUS",""),
    HYPNUS("HYPNUS",""),
    MINOTAUR("MINOTAUR","Bull-headed Monster\nYour Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."),
    MORTAL("MORTAL",""),
    PAN("PAN",""),
    POSEIDON("POSEIDON",""),
    PROMETHEUS("PROMETHEUS","Titan Benefactor of Mankind\nYour Turn: If your Worker does not move up, it may build both before and after moving."),
    TRITON("TRITON",""),
    ZEUS("ZEUS","");

    God(String godName, String power){
        this.godName = godName;
        this.power = power;
    }

    @Override
    public String toString(){
        return godName;
    }

    public String getPower(){
        return power;
    }


    private String godName;
    private String power;

}

package it.polimi.ingsw.model;

public enum God {
    APOLLO("APOLLO",""),
    ARTEMIS("ARTEMIS",""), 
    ATHENA("ATHENA",""), 
    ATLAS("ATLAS",""), 
    CHARON("CHARON",""), 
    DEMETER("DEMETER",""), 
    HEPHAESTUS("HEPHAESTUS",""),
    HYPNUS("HYPNUS",""),
    MINOTAUR("MINOTAUR",""),
    MORTAL("MORTAL",""),
    PAN("PAN",""),
    POSEIDON("POSEIDON",""),
    PROMETHEUS("PROMETHEUS",""),
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
    //private image;

}

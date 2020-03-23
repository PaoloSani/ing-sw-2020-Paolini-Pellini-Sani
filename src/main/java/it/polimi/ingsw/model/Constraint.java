package it.polimi.ingsw.model;

public class Constraint
{

    private boolean Athena;
    private boolean Hypnus;

    public boolean athenaBlocks() {
        return Athena;
    }
    public boolean hypnusBlocks() {
        return Hypnus;
    }

    public void setHypnus(boolean hypnus) {
        Hypnus = hypnus;
    }

    public void setAthena(boolean athena) {
        Athena = athena;
    }


    public Constraint() {
        Athena = false;
        Hypnus = false;
    }




}

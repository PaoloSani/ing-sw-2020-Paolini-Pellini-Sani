package it.polimi.ingsw.model;

public class Constraint
{
    /*
      La classe è istanziata ogni volta che è presente in gioco o Atena o Ipnos. Atena viene utilizzata
      nella move se il flag è vero allora tutti i giocatori avversari non possono salira al livello successivo.

      Se Ipnos è presenti i workers avversari non possono muovere il worker più alto.

     */

    private boolean Athena;
    private boolean Hypnus;

    // Getter
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

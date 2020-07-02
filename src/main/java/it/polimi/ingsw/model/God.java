package it.polimi.ingsw.model;

/**
 * The God enum is the list of gods available in the game
 */
public enum God {
    APOLLO("APOLLO","God Of Music\nYour Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated."),
    ARTEMIS("ARTEMIS","Goddess of the Hunt\nYour Move: Your Worker may move one additional time, but not back to its initial space."),
    ATHENA("ATHENA","Goddess of Wisdom\nOpponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn."),
    ATLAS("ATLAS","Titan Shouldering the Heavens\n Your Build: Your Worker may build a dome at any level."),
    CHARON("CHARON","Your Move: Before your Worker moves, you may force a neighboring opponent Worker to the space directly on the other side of your Worker, if that space is unoccupied."),
    DEMETER("DEMETER","Goddess of the Harvest\nYour Build: Your Worker may build one additional time, but not on the same space."),
    HEPHAESTUS("HEPHAESTUS","God of Blacksmiths\nYour Build: Your Worker may build one additional block (not dome) on top of your first block."),
    HYPNUS("HYPNUS","God of Sleep\nStart of Opponent’s Turn: If one of your opponent’s Workers is higher than all of their others, it cannot move."),
    MINOTAUR("MINOTAUR","Bull-headed Monster\nYour Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level."),
    MORTAL("MORTAL","The Twelve Labours of Heracles are nothing for you. Prove other players you're a real Hero!"),
    PAN("PAN","God of the Wild\nWin Condition: You also win if your Worker moves down two or more levels."),
    POSEIDON("POSEIDON","God of the Sea\nEnd of Your Turn: If your unmoved Worker is on the ground level, it may build up to three times."),
    PROMETHEUS("PROMETHEUS","Titan Benefactor of Mankind\nYour Turn: If your Worker does not move up, it may build both before and after moving."),
    TRITON("TRITON","Your Move: Each time your Worker moves into a perimeter space, it may immediately move again."),
    ZEUS("ZEUS","God of the Sky\nYour Build: Your Worker may build a block under itself.");

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

    private final String godName;
    private final String power;
}

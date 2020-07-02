package it.polimi.ingsw.model;

/**
 * Player is used to keep trace of a player in the match
 */
public class Player  {
    private final String nickname;
    private God god;
    private final Worker worker1;
    private final Worker worker2;
    private Move move;
    private Build build;
    private final Game game;

    /**
     * Player's constructor
     * @param nickname : the nickname of the player
     * @param god : the player's god
     * @param game : reference to the Game class
     */
    public Player( String nickname, God god, Game game) {
        this.nickname = nickname;
        this.game = game;
        setGod(god);
        this.worker1 = new Worker(this );
        this.worker2 = new Worker(this );
    }

    public String getNickname() {
        return nickname;
    }

    public Worker getWorker1() {
        return worker1;
    }

    public Worker getWorker2() {
        return worker2;
    }

    public God getGod() {
        return god;
    }

    public Game getGame() {
        return game;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    /**
     * Returns the second worker, given the first one
     * @param worker : the first worker
     * @return : the other worker
     */
    public Worker getOtherWorker( Worker worker ){
        if ( worker == this.worker1 ){
            return this.worker2;
        }
        else return this.worker1;
    }


    /**
     * Sets the player's god and according to that, his move and build strategies or constraints
     * @param god : the god to set
     */
    public void setGod(God god) {
        this.god = god;

        switch ( god ) {
            case APOLLO:
                setMove( new MoveApollo() );
                setBuild( new BuildDefault() );
                break;

            case ATLAS:
                setMove( new MoveDefault() );
                setBuild( new BuildAtlas() );
                break;


            case MINOTAUR:
                setMove( new MoveMinotaur() );
                setBuild(new BuildDefault());
                break;

            case ZEUS:
                setMove( new MoveDefault() );
                setBuild( new BuildZeus() );
                break;

            // Artemis, Tritone, Demetra, Pan, Caronte, Atena, Poseidone, Ipnos, Prometeo, Efesto
            default :   setMove(new MoveDefault());
                setBuild(new BuildDefault());

        }

        //Hypnus has default move and build strategies, but I must set his constraint
        if( god == God.HYPNUS ){
            this.game.getConstraint().setHypnus(true);
        }
    }


    /**
     * The isWinner method is invoked after each move and checks if the move is winning.
     * In case of victory, the winner parameter is set in the Litegame
     * @param oldSpace Space where the current worker was before the move
     * @param currSpace Space where the current worker is currently located
     */
    public void isWinner(Space oldSpace, Space currSpace) {
        if ( oldSpace.getHeight() == 2 && currSpace.getHeight() == 3 ) {
            game.getLiteGame().setWinner(true);
        }
        else {
            //Pan's winning condition
            game.getLiteGame().setWinner(this.god == God.PAN && (oldSpace.getHeight() - currSpace.getHeight() > 1));
        }
    }

    /**
     * Executes the move strategy
     * @param worker worker to move
     * @param destination space where to move
     * @return true if the action was correctly performed
     */
    public boolean moveWorker(Worker worker, Space destination) {
        return this.move.execute(worker, destination);
    }

    /**
     * Executes the build strategy
     * @param worker the worker which builds
     * @param spaceToBuild space where to build
     * @param level level to build
     * @return true if the action was correctly performed
     */
    public boolean buildSpace(Worker worker, Space spaceToBuild, int level) {
        return this.build.execute(worker, spaceToBuild, level);
    }

}


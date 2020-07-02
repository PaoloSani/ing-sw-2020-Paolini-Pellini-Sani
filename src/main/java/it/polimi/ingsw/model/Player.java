package it.polimi.ingsw.model;

/**
 * Class used to keep track of players in a game
 */
public class Player  {
    private final String nickname;
    private God god;
    private final Worker worker1;
    private final Worker worker2;
    private Move move;
    private Build build;
    private final Game game;

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

    //utile per choosingWorker nel controller e per il potere di Poseidone
    public Worker getOtherWorker( Worker worker ){
        if ( worker == this.worker1 ){
            return this.worker2;
        }
        else return this.worker1;
    }



    //setta la divinità e in base a quella sceglie le varie strategy associate al player
    public void setGod(God god) {
        this.god = god;
        /*
            In base alla divinità scelgo le varie strategies da utilizzare
        */
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

        //l'altro caso che posso aver è che la divinità sia Hypnus, quindi vado a settare la classe costraint se è presente
        if( god == God.HYPNUS ){
            this.game.getConstraint().setHypnus(true);
        }
    }


    //Il metodo isWinner viene invocato successivamente ad ogni move() e controlla se la mossa è vincente
    //Al suo interno chiama la scrittura nel liteGame per segnalare se il giocatore corrente ha vinto

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
            //Condizione di vittoria Pan
            game.getLiteGame().setWinner(this.god == God.PAN && (oldSpace.getHeight() - currSpace.getHeight() > 1));
        }
    }

    // Esegue la strategy che viene associata ad ogni player secondo la propria divinità
    public boolean moveWorker(Worker worker, Space destination) {
        return this.move.execute(worker, destination);
    }

    // Esegue la strategy che viene associata ad ogni player secondo la propria divinità
    public boolean buildSpace(Worker worker, Space spaceToBuild, int level) {
        return this.build.execute(worker, spaceToBuild, level);
    }

}


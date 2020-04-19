package it.polimi.ingsw.model;



public class Player {
    private final String nickname;
    private God god;
    private Worker worker1;
    private Worker worker2;
    private Move move;
    private Build build;
    private Game game;

    public Player( String nickname, God god, Game game) {
        this.nickname = nickname;
        setGod(god);
        this.worker1 = new Worker(this );
        this.worker2 = new Worker(this );
        this.game = game;
        //scrive i suoi dati nel messaggio Game.message
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
    public boolean isWinner(Space oldSpace, Space currSpace) {
        if ( oldSpace.getHeight() == 2 && currSpace.getHeight() == 3 )
            return true;
        //Condizione di vittoria Pan
        else return this.god == God.PAN && (oldSpace.getHeight() - currSpace.getHeight() > 1 );
    }

    // Esegue la strategy che viene associata ad ogni player secondo la propria divinità
    public boolean moveWorker(Worker worker, Space destination) throws IllegalSpaceException {
        return this.move.execute(worker, destination);
    }

    // Esegue la strategy che viene associata ad ogni player secondo la propria divinità
    public void buildSpace(Worker worker, Space spaceToBuild, int level) throws IllegalSpaceException {
        this.build.execute(worker, spaceToBuild, level);
    }


}


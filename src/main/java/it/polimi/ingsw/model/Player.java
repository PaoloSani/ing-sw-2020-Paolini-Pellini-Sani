package it.polimi.ingsw.model;



public class Player {
    private final String nickname;
    private String godName;
    private Worker worker1;
    private Worker worker2;
    private Move move;
    private Build build;
    private Model model;



    public void setMove(Move move) {
        this.move = move;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public Player( String nickname, Model model ) {
        this.nickname = nickname;
        this.godName = null;
        this.worker1 = new Worker(this );
        this.worker2 = new Worker(this  );
        this.move = null;
        this.build = null;
        this.model = model;
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

    public String getGodName() {
        return godName;
    }

    public Model getModel() {
        return model;
    }

    public void setGodName(String godName) {
        this.godName = godName;
        /*
            In base alla divinità scelgo le varie strategies da utilizzare
        */
        switch ( godName ) {
            case "Apollo":
                setMove( new MoveApollo() );
                setBuild( new BuildDefault() );
                break;

            case "Atlas":
                setMove( new MoveDefault() );
                setBuild( new BuildAtlas() );
                break;

            case "Hephaestus":
                setMove( new MoveDefault() );
                setBuild( new BuildHephaestus() );
                break;

            case "Minotaur":
                setMove( new MoveMinotaur() );
                setBuild(new BuildDefault());
                break;

            case "Zeus":
                setMove( new MoveDefault() );
                setBuild( new BuildZeus() );
                break;

            // Artemis, Tritone, Demetra, Pan, Caronte, Atena, Poseidone, Ipnos, Prometeo
            default :   setMove(new MoveDefault());
                setBuild(new BuildDefault());

        }
    }

    /*
        Il metodo isWinner viene invocato successivamente ad ogni move() e controlla se la mossa è vincente
     */

    public boolean isWinner(Space oldSpace, Space currSpace) {
        if ( oldSpace.getHeight() == 2 && currSpace.getHeight() == 3 )
            return true;
        //Condizione di vittoria Pan
        else return this.godName == "Pan" && (oldSpace.getHeight() - currSpace.getHeight() > 1 );
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


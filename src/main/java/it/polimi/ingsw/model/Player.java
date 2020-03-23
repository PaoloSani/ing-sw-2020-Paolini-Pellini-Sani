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
            default :   setMove(new MoveDefault());
                        setBuild(new BuildDefault());

        }
    }

    public boolean isWinner(Space oldSpace, Space currSpace) {
        if ( oldSpace.getHeight() == 2 && currSpace.getHeight() == 3 )
            return true;
        else return this.godName == "Pan" && (oldSpace.getHeight() - currSpace.getHeight() > 1 );
    }

    public boolean moveWorker(Worker worker, Space space) throws IllegalSpaceException {
        return this.move.execute(worker, space);
    }

    public void buildSpace(Worker worker, Space space, int level) throws IllegalSpaceException {
        this.build.execute(worker, space, level);
    }


}


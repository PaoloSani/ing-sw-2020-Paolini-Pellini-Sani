package it.polimi.ingsw.model;

public class Player {
    private final String nickname;
    private String godName;
    private Worker worker1;
    private Worker worker2;
    private Move move;
    private Build build;
    private Model model;



    public void setMove() {
        this.move = move;
    }

    public void setBuild() {
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

       // switch (godName) ...
    }

    public boolean isWinner(Space oldSpace, Space currSpace) {

    }

    public boolean moveWorker(Worker worker, Space space){
        this.move.execute(worker, space);
    }

    public void buildSpace(Worker worker, Space space, int level){
        this.build.execute(worker, space, level);
    }


}


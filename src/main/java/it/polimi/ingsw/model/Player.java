package it.polimi.ingsw.model;

public class Player {
    private final String nickname;
    private Worker worker1;
    private Worker worker2;
    private String godName;

    public Player( String nickname ) {
        this.nickname = nickname;
        this.worker1 = new Worker(this );
        this.worker2 = new Worker(this  );

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

    public void setGodName(String godName) {
        this.godName = godName;

       // switch (godName) ...
    }

}


package it.polimi.ingsw.Model;

public class Player {
    private final String nickname;
    private Worker worker1;
    private Worker worker2;

    public Player(String nickname, Worker worker1, Worker worker2) {
        this.nickname = nickname;
        this.worker1 = worker1;
        this.worker2 = worker2;
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

    public Worker chooseWorker()


}


package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientMessage;
import it.polimi.ingsw.client.SettingGameMessage;
import it.polimi.ingsw.model.LiteGame;

import java.io.*;
import java.net.Socket;


public class SocketClientConnection implements Runnable {
    private Socket socket;
    private Server server;
    private boolean active = true;
    private String name;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }
    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
       //server.deregisterConnection(this);
        System.out.println("Done!");
    }

    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }
    @Override
    public void run() {
        name = null;
        int playersInTheGame = 0;
        int gameID;
        boolean validMessage = false;

        try{
            send("Welcome!\n");
            SettingGameMessage settings = new SettingGameMessage();

            while ( name == null ){
                try {
                    settings = (SettingGameMessage) in.readObject();
                } catch(IOException | ClassNotFoundException e){
                    send("Error in serialization!\n");
                }

                if ( server.existingNickname(settings.getNickname()) )
                    send("Nickname not available!\n");
                else {
                    name = settings.getNickname();
                    send("Nickname accepted!\n");
                }
            }

            while (!validMessage) {

                try {
                    settings = (SettingGameMessage) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    send("Error in serialization!\n");
                }

                if (settings.isCreatingNewGame()) {
                    validMessage = true;
                    server.updateCurrMatch();                       //Creato il game ID aggiornato
                    settings.setGameID(server.getCurrMatch());      //Passiamo al client l'ID aggiornato

                    playersInTheGame = settings.getNumberOfPlayer();
                    server.createMatch(server.getCurrMatch(), playersInTheGame, this);

                    // Nel server crea un nuovo GameID e aspetta che i giocatori si colleghino
                    // legge quanti giocatori mettere nella partita
                } else if (settings.isPlayingExistingMatch()) {
                    gameID = settings.getGameID();
                    if (server.addPlayer(gameID,this) && server.checkMatch(gameID)) server.startGame(gameID);
                    validMessage = true;
                    //Se il giocatore viene aggiunto
                    //controllo se è possibile runnare una nuova partita

                } else {
                    server.lobby(settings.getNickname(), playersInTheGame, this);
                    validMessage = true;
                    //il server lo inserisce nella waitingConnection tenendo
                    // conto del numero di giocatori con cui vuole giocare
                }
            }

            //server.lobby(this, name);



            //Crea nuova Partita - Carica partita esistente - Inizia a giocare (opzione con cui gioco con giocatori casuali)



            /*server.lobby(this, name);
            while(isActive()){
                read = in.nextLine();
                notify(read);
            }*/

  /*      } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] readChallengerMessage() {
        String message = null;
        try {
            message = (String) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //restituisce un array di stringhe, separando in modo opportuno message secondo la presenza di virgole
        return (message.split(",")); 
    }

    public ClientMessage readClientMessage() {
        ClientMessage message = null;
        try {
            message = (ClientMessage) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }


    public String getName() {
        return name;
    }

}


  /*  ByteArrayOutputStream os = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(os);
        out.reset();
                out.writeObject(pIn);
                out.flush();
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                ObjectInputStream in = new ObjectInputStream(is);
                Person pOut = (Person)in.readObject();
                System.out.println(pOut.getName());
                System.out.println(pOut.getBirthday().getTime());
                System.out.println(pOut.getAge());
*/
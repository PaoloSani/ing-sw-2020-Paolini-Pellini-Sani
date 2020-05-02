package it.polimi.ingsw.server;

import it.polimi.ingsw.client.SettingGameMessage;

import java.io.*;
import java.net.Socket;
import java.nio.channels.SeekableByteChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClientConnection implements ClientConnection, Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private Server server;
    private boolean active = true;
    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
    private synchronized boolean isActive(){
        return active;
    }
    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    @Override
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
    @Override
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
        ObjectInputStream in;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream is;
        String name = null;
        int playersInTheGame = 0;
        int gameID;

        try{
            is = new ByteArrayInputStream(os.toByteArray());
            in = new ObjectInputStream(is);
            out = new ObjectOutputStream(socket.getOutputStream());
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

            is = new ByteArrayInputStream(os.toByteArray());
            in = new ObjectInputStream(is);

            try {
                settings = (SettingGameMessage) in.readObject();
            } catch(IOException | ClassNotFoundException e){
                send("Error in serialization!\n");
            }

            if ( settings.isCreatingNewGame() ){
                //nel server crea un nuovo GameID e aspetta che i giocatori si colleghino
                // legge quanti giocatori mettere nella partita
            }
            else if (settings.isPlayingExistingMatch()){
                //prende
                settings.getGameID();
                // lo inserisce nella tale partita nel server

            }
            else {
                //il server lo inserisce nella waitingConnection tenendo
                // conto del numero di giocatori con cui vuole giocare
            }


            //server.lobby(this, name);



            //Crea nuova Partita - Carica partita esistente - Inizia a giocare (opzione con cui gioco con giocatori casuali)



            /*server.lobby(this, name);
            while(isActive()){
                read = in.nextLine();
                notify(read);
            }*/

        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();
        }
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
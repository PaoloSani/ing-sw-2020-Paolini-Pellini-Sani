package it.polimi.ingsw.model;

public class IllegalSpaceException extends Exception{
    public IllegalSpaceException() {
        super();
    }

    public IllegalSpaceException( String message ) {
        super( message );
    }
}
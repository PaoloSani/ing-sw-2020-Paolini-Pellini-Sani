package it.polimi.ingsw.model;

@Deprecated
public class IllegalSpaceException extends Exception{
    public IllegalSpaceException() {
        super();
    }

    public IllegalSpaceException( String message ) {
        super( message );
    }
}
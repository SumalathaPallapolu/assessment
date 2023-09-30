package co.fullstacklabs.battlemonsters.challenge.exceptions;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class UnprocessableFileException extends RuntimeException {

    public static final long serialVersionUID = 4328745;

    public UnprocessableFileException(String message){
        super(message);
    }
}

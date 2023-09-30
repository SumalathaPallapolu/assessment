package co.fullstacklabs.battlemonsters.challenge.exceptions;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class ResourceNotFoundException extends RuntimeException{
    public static final long serialVersionUID = 4328743;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}

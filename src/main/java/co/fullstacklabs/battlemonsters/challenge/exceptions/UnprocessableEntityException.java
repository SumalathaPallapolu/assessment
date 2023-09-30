package co.fullstacklabs.battlemonsters.challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException{

    public static final long serialVersionUID = 4328744;

    public UnprocessableEntityException(String message) {
        super(message);
    }
}

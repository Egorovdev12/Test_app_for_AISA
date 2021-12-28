package egorovIVSolution.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There are not enough ingredients for the preparation of this drink")
public class OutOfResourceException extends RuntimeException {
    public OutOfResourceException() {

    }
}
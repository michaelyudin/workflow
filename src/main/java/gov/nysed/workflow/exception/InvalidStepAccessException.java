package gov.nysed.workflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidStepAccessException extends RuntimeException {

    public InvalidStepAccessException(String message) {
        super(message);
    }
}

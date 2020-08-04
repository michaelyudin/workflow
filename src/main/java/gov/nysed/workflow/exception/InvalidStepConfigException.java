package gov.nysed.workflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidStepConfigException extends RuntimeException {

    public InvalidStepConfigException(String message) {
        super(message);
    }
}

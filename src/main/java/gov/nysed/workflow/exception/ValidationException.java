package gov.nysed.workflow.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ValidationException extends RuntimeException {
    private final Set<ConstraintViolation<Object>> errors;

    public ValidationException(String s, Set<ConstraintViolation<Object>> errors) {
        super(s);
        this.errors = errors;
    }

    public Set<ConstraintViolation<Object>> getErrors() {
        return errors;
    }
}

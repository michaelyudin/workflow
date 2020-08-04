package gov.nysed.workflow.util;


import gov.nysed.workflow.exception.ValidationException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RequestUtil {

    /**
     * Bind the request body to a passed in POJO
     * @param object
     * @return
     */
    public static Object bindPostToObject(Object object) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if (request.getParameter("_step") != null) {
            ServletRequestDataBinder binder = new ServletRequestDataBinder(object);
            binder.bind(request);
        }

        return object;
    }

    /**
     * Determine the current step from the Request.
     *
     * @return
     */
    public static String getCurrentStep() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getParameter("_step") != null ? request.getParameter("_step") : "";
    }

    /**
     * Is the step passed in the current one being processed?
     *
     * @param step
     * @return
     */
    public static boolean isCurrentStep(String step) {
        return RequestUtil.getCurrentStep().equals(step);
    }

    /**
     * Determine the current step from the Request.
     *
     * @return
     */
    public static UUID getResultUuid() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getParameter("_result_uuid") != null ? UUID.fromString(request.getParameter("_result_uuid")) : null;
    }

    /**
     * Leverage jpa validator to validate an object.
     *
     * @param object
     */
    public static void validate(Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // @TODO: handle validation gracefully
        Set<ConstraintViolation<Object>> errors = validator.validate(object);

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation on request failed.", errors);
        }
    }
}

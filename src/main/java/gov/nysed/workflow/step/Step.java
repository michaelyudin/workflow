package gov.nysed.workflow.step;

import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.util.RequestUtil;

public interface Step {

    /**
     * Process the logic in a step.
     *
     * @param result
     * @return
     */
    StepResult runStep(WorkflowResult result);

    /**
     * Determine if this step should be processed.
     * Override if you have custom logic that should determine whether
     * or not a step should be run.
     *
     * @return
     */
    default boolean isStepValid(WorkflowResult result) {
        return true;
    }

    /**
     * Each step in a flow should have a unique name.
     *
     * @return
     */
    String getName();

    /**
     * The name of the WorkflowEvent that determines whether this step has already processed.
     *
     * @return
     */
    String getCompletedEventName();

    /**
     * Binds the Request data to an object and then runs validators against the object.
     * <p>
     * This replaces @ModelAttribute and @Validated annotations.
     *
     * @param object
     * @return
     */
    default Object bindAndValidate(Object object) {
        object = RequestUtil.bindPostToObject(object);
        RequestUtil.validate(object);

        return object;
    }
}
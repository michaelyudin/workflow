package gov.nysed.workflow;

import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.step.Step;

public interface Workflow {

    default Step getInitialStep(WorkflowResult result) {
        WorkflowBuilder builder = this.getBuilder();
        return builder.getSteps().get(builder.getStepNames().stream().findFirst().orElse(null));
    }

    /**
     * When this event is found, this workflow can no longer add events.
     *
     * @return
     */
    default String getTerminalEventName() {
        WorkflowBuilder builder = this.getBuilder();

        Step lastStep = builder.getSteps().get(builder.getStepNames().get(builder.getStepNames().size() - 1));
        return lastStep.getCompletedEventName();
    }

    /**
     * Unique name for this workflow that can be identified by the PathVariable in WorkflowController
     * @return
     */
    String getName();

    WorkflowBuilder getBuilder();

    /**
     * When continuing a Workflow, should users start at the last step completed
     * or be forced to start at the first step again.
     * @return
     */
    default boolean continueAtLastStepCompleted() {
        return false;
    }

}
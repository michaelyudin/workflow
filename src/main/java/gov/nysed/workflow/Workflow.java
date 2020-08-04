package gov.nysed.workflow;

import gov.nysed.workflow.domain.entity.WorkflowResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface Workflow {


    default Step getInitialStep(WorkflowResult result) {
        WorkflowBuilder builder = this.getBuilder();
        return builder.getSteps().get(builder.getStepNames().stream().findFirst().orElse(null));
    };

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
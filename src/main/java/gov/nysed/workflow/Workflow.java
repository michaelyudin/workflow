package gov.nysed.workflow;

import java.util.LinkedList;
import java.util.List;

public interface Workflow {
    default List<String> templateOverrides() {
        return new LinkedList<>();
    }

    List<Step> getSteps();

    /**
     * Unique name for this workflow that can be identified by the PathVariable in WorkflowController
     * @return
     */
    String getName();

    /**
     * When continuing a Workflow, should users start at the last step completed
     * or be forced to start at the first step again.
     * @return
     */
    default boolean continueAtLastStepCompleted() {
        return false;
    }
}
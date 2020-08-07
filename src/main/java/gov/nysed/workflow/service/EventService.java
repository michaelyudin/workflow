package gov.nysed.workflow.service;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowEventType;
import gov.nysed.workflow.domain.entity.WorkflowResult;

import java.util.List;
import java.util.Optional;

public interface EventService {

    String CONTINUE_EVENT = "WORKFLOW_CONTINUED";

    /**
     * Insert a new Event in to the EVENTS table
     * @param event
     * @return
     */
    WorkflowEvent createEvent(WorkflowResult result, String eventType, String eventName);

    /**
     * Insert a new EventType in the EVENT_TYPE table.
     *
     * @param eventType
     * @return
     */
    WorkflowEventType createEventType(WorkflowEventType eventType);

    /**
     * Find an EventType by enum/string
     * @param eventType
     * @return
     */
    Optional<WorkflowEventType> findEventType(String eventType);

    /**
     * Trigger a continue event for a specific result.
     *
     * @param result
     * @return
     */
    WorkflowEvent createContinueEvent(WorkflowResult result);

    /**
     * Return all events since the date of the last continue OR the results create date if there is no continue.
     *
     * @param result
     * @return
     */
    List<WorkflowEvent> getEventsSinceLastContinue(WorkflowResult result);
}

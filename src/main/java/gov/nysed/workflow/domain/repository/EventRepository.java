package gov.nysed.workflow.domain.repository;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowEventType;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<WorkflowEvent, UUID> {

    /**
     * Finds the last occurrence of a specific event type for a result.
     *
     * @param result
     * @param eventType
     * @return
     */
    WorkflowEvent findTopByResultAndEventTypeOrderByDateCreatedDesc(WorkflowResult result, WorkflowEventType eventType);

    /**
     * Find all events after a specific time for a result.
     *
     * @param dateCreated
     * @return
     */
    List<WorkflowEvent> findAllByResultAndDateCreatedAfterOrderByDateCreated(WorkflowResult result, Date dateCreated);
}

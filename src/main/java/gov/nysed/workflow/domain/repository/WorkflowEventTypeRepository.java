package gov.nysed.workflow.domain.repository;

import gov.nysed.workflow.domain.entity.WorkflowEventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowEventTypeRepository extends JpaRepository<WorkflowEventType, String> {
}

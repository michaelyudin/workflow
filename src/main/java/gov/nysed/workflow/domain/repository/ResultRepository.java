package gov.nysed.workflow.domain.repository;

import gov.nysed.workflow.domain.entity.WorkflowResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResultRepository extends JpaRepository<WorkflowResult, UUID> {
}

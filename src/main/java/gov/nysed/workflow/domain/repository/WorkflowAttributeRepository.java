package gov.nysed.workflow.domain.repository;

import gov.nysed.workflow.domain.entity.WorkflowAttribute;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WorkflowAttributeRepository extends JpaRepository<WorkflowAttribute, UUID> {

    @Query("SELECT result FROM WorkflowResult result LEFT JOIN FETCH result.events WHERE result.id = :id")
    Optional<WorkflowResult> findWorkflowResultById(UUID id);
}

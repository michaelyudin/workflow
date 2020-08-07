package gov.nysed.workflow.domain.repository;

import gov.nysed.workflow.domain.entity.WorkflowIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowIdentifierRepository extends JpaRepository<WorkflowIdentifier, UUID> {

    Optional<WorkflowIdentifier> findWorkflowConfigBySlug(String slug);
}

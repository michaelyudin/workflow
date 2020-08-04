package gov.nysed.workflow.domain.repository;

import gov.nysed.workflow.domain.entity.WorkflowConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowRepository extends JpaRepository<WorkflowConfig, UUID> {

    Optional<WorkflowConfig> findWorkflowConfigBySlug(String slug);
}

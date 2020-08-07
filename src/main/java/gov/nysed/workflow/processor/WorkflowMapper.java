package gov.nysed.workflow.processor;

import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.domain.entity.WorkflowIdentifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Manages a map(name -> Workflow) of available workflows by retrieving all
 * Services implementing Workflow so that workflows can be pulled dynamically.
 */
@Service
public class WorkflowMapper {
    private Map<String, Workflow> workflows = new HashMap<>();

    public WorkflowMapper(List<Workflow> workflows) {
        workflows.forEach(workflow -> this.workflows.put(workflow.getName(), workflow));
    }

    public Optional<Workflow> getWorkflow(WorkflowIdentifier workflowIdentifier) {
        return Optional.ofNullable(workflows.getOrDefault(workflowIdentifier.getSlug(), null));
    }

    public Map<String, Workflow> getWorkflows() {
        return workflows;
    }
}

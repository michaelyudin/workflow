package gov.nysed.workflow;

import gov.nysed.workflow.domain.entity.WorkflowIdentifier;
import gov.nysed.workflow.domain.repository.WorkflowIdentifierRepository;
import gov.nysed.workflow.processor.WorkflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class WorkflowConfigurator {

    @Autowired
    private WorkflowIdentifierRepository workflowIdentifierRepository;

    @Autowired
    private WorkflowMapper workflowMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        workflowMapper.getWorkflows().forEach((s, workflow) -> {
            workflowIdentifierRepository
                    .findWorkflowConfigBySlug(workflow.getName())
                    .orElse(createWorkflowConfig(workflow));
        });
    }

    private WorkflowIdentifier createWorkflowConfig(Workflow workflow) {
        WorkflowIdentifier workflowIdentifier = new WorkflowIdentifier();
        workflowIdentifier.setSlug(workflow.getName());
        workflowIdentifierRepository.save(workflowIdentifier);

        return workflowIdentifier;
    }
}

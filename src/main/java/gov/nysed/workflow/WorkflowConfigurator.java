package gov.nysed.workflow;

import gov.nysed.workflow.domain.entity.WorkflowConfig;
import gov.nysed.workflow.domain.repository.EventTypeRepository;
import gov.nysed.workflow.domain.repository.WorkflowRepository;
import gov.nysed.workflow.processor.WorkflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class WorkflowConfigurator {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowMapper workflowMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        workflowMapper.getWorkflows().forEach((s, workflow) -> {
            workflowRepository
                    .findWorkflowConfigBySlug(workflow.getName())
                    .orElse(createWorkflowConfig(workflow));
        });
    }

    private WorkflowConfig createWorkflowConfig(Workflow workflow) {
        WorkflowConfig workflowConfig = new WorkflowConfig();
        workflowConfig.setSlug(workflow.getName());
        workflowRepository.save(workflowConfig);

        return workflowConfig;
    }
}

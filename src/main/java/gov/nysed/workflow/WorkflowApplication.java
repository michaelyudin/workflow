package gov.nysed.workflow;

import gov.nysed.workflow.domain.entity.WorkflowConfig;
import gov.nysed.workflow.domain.repository.EventTypeRepository;
import gov.nysed.workflow.domain.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WorkflowApplication {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private WorkflowRepository formRepository;

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        WorkflowConfig workflow = new WorkflowConfig();

        workflow.setSlug("replace-reg");

        formRepository.save(workflow);
    }
}

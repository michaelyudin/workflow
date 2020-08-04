package gov.nysed.workflow.example.step;

import gov.nysed.workflow.step.Step;
import gov.nysed.workflow.step.WebStepResult;
import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowEventType;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.example.domain.AttestationEvent;
import gov.nysed.workflow.example.domain.AttestationRepository;
import gov.nysed.workflow.service.EventService;
import gov.nysed.workflow.util.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class AttestationStep implements Step {

    private static final String COMPLETED_EVENT_NAME = "ATTESTATION_COMPLETED";

    private EventService eventService;

    private AttestationRepository attestationRepository;

    public AttestationStep(EventService eventService, AttestationRepository attestationRepository) {
        this.eventService = eventService;
        this.attestationRepository = attestationRepository;
    }

    @Override
    public WebStepResult runStep(WorkflowResult result) {

        ModelAndView view = new ModelAndView("attestation");

        view.addObject("result", result);

        // process the step response.
        if (RequestUtil.isCurrentStep(this.getName())) {
            AttestationEvent event = new AttestationEvent();
            this.bindAndValidate(event);
            createCompletedEvent(result, event);
            return new WebStepResult("ATTESTATION_COMPLETED", null, true);
        }

        return new WebStepResult("ATTESTATION_LOADED", view, false);
    }

    @Override
    public String getName() {
        return "attestation";
    }

    @Override
    public boolean isStepValid(WorkflowResult result) {
        return result.getWorkflowConfig().getSlug() == "delayed-reg";
    }

    @Override
    public String getCompletedEventName() {
        return COMPLETED_EVENT_NAME;
    }


    private WorkflowEvent createCompletedEvent(WorkflowResult result, AttestationEvent attestationEvent) {
        WorkflowEvent event = new WorkflowEvent();
        event.setResult(result);
        event.setEventType(eventService.findEventType(COMPLETED_EVENT_NAME).orElse(createCompletedEventType()));

        event = eventService.createEvent(event);

        attestationEvent.setEvent(event);

        attestationRepository.save(attestationEvent);

        return event;
    }

    private WorkflowEventType createCompletedEventType() {
        WorkflowEventType eventType = new WorkflowEventType();
        eventType.setEventType(COMPLETED_EVENT_NAME);
        eventType.setEventName("Attestation Completed");

        return eventService.createEventType(eventType);
    }
}

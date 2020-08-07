package gov.nysed.workflow.example.step;

import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.service.EventService;
import gov.nysed.workflow.step.Step;
import gov.nysed.workflow.step.StepResult;
import gov.nysed.workflow.step.WebStepResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ThankYouStep implements Step {

    @Autowired
    private EventService eventService;

    private static final String COMPLETED_EVENT_NAME = "WORKFLOW_COMPLETED";

    @Override
    public StepResult runStep(WorkflowResult result) {
        eventService.createEvent(result, COMPLETED_EVENT_NAME, "Workflow Completed");
        ModelAndView view = new ModelAndView("thank-you");
        return new WebStepResult("THANK_YOU_LOADED", view, false);
    }

    @Override
    public String getName() {
        return "thank-you";
    }

    @Override
    public String getCompletedEventName() {
        return COMPLETED_EVENT_NAME;
    }

}

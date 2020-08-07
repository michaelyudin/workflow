package gov.nysed.workflow.example.step;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.example.domain.DataServiceResult;
import gov.nysed.workflow.service.EventService;
import gov.nysed.workflow.step.Step;
import gov.nysed.workflow.step.StepResult;
import gov.nysed.workflow.step.WebStepResult;
import gov.nysed.workflow.util.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class DataServiceFormStep implements Step {

    private static final String STARTED_EVENT_NAME = "RESULT_STARTED";
    private static final String COMPLETED_EVENT_NAME = "RESULT_CAPTURED";

    private EventService eventService;

    public DataServiceFormStep(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public StepResult runStep(WorkflowResult result) {

        ModelAndView view = new ModelAndView("result");

        // process the step response.
        if (RequestUtil.isCurrentStep(this.getName())) {
            eventService.createEvent(result, COMPLETED_EVENT_NAME, "Result Completed");

            return new WebStepResult("RESULT_COMPLETED", null, true);
        } else {
            WorkflowEvent startedEvent = result.getEvents()
                    .stream()
                    .filter(workflowEvent -> workflowEvent.getEventType().equals(STARTED_EVENT_NAME))
                    .findFirst()
                    .orElse(eventService.createEvent(result, STARTED_EVENT_NAME, "Result Started"));

            DataServiceResult dataServiceResult = new DataServiceResult();
            dataServiceResult.setEvent(startedEvent);
            dataServiceResult.setDataServiceResultId("1234");
        }

        view.addObject("result", result);

        return new WebStepResult("RESULT_STARTED", view, false);
    }

    @Override
    public String getName() {
        return "result";
    }

    @Override
    public String getCompletedEventName() {
        return COMPLETED_EVENT_NAME;
    }
}

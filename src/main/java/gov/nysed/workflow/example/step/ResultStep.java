package gov.nysed.workflow.example.step;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowEventType;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.example.domain.DataServiceResult;
import gov.nysed.workflow.service.EventService;
import gov.nysed.workflow.util.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ResultStep implements Step {

    private static final String STARTED_EVENT_NAME = "RESULT_STARTED";
    private static final String COMPLETED_EVENT_NAME = "RESULT_CAPTURED";

    private EventService eventService;

    public ResultStep(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public ModelAndView runStep(WorkflowResult result) {

        ModelAndView view = new ModelAndView("result");

        // process the step response.
        if (RequestUtil.isCurrentStep(this.getName())) {
            createCompletedEvent(result);
        } else {
            WorkflowEvent startedEvent = result.getEvents()
                    .stream()
                    .filter(workflowEvent -> workflowEvent.getEventType().equals(STARTED_EVENT_NAME))
                    .findFirst()
                    .orElse(createStartedEvent(result));

            DataServiceResult dataServiceResult = new DataServiceResult();
            dataServiceResult.setEvent(startedEvent);
            dataServiceResult.setDataServiceResultId("1234");
        }

        view.addObject("result", result);

        return view;
    }

    @Override
    public String getName() {
        return "result";
    }

    @Override
    public String getCompletedEventName() {
        return COMPLETED_EVENT_NAME;
    }

    private WorkflowEvent createStartedEvent(WorkflowResult result) {
        WorkflowEvent event = new WorkflowEvent();
        event.setResult(result);
        event.setEventType(eventService.findEventType(STARTED_EVENT_NAME).orElse(createStartedEventType()));

        return eventService.createEvent(event);
    }

    private WorkflowEventType createStartedEventType() {
        WorkflowEventType eventType = new WorkflowEventType();
        eventType.setEventType(STARTED_EVENT_NAME);
        eventType.setEventName("Result Started");

        return eventService.createEventType(eventType);
    }

    private WorkflowEvent createCompletedEvent(WorkflowResult result) {
        WorkflowEvent event = new WorkflowEvent();
        event.setResult(result);
        event.setEventType(eventService.findEventType(COMPLETED_EVENT_NAME).orElse(createCompletedEventType()));

        return eventService.createEvent(event);
    }

    private WorkflowEventType createCompletedEventType() {
        WorkflowEventType eventType = new WorkflowEventType();
        eventType.setEventType(COMPLETED_EVENT_NAME);
        eventType.setEventName("Result Completed");

        return eventService.createEventType(eventType);
    }
}

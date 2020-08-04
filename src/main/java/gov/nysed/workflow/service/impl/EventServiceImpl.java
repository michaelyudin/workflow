package gov.nysed.workflow.service.impl;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowEventType;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.domain.repository.EventRepository;
import gov.nysed.workflow.domain.repository.EventTypeRepository;
import gov.nysed.workflow.service.EventService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    private EventTypeRepository eventTypeRepository;

    public EventServiceImpl(EventRepository eventRepository, EventTypeRepository eventTypeRepository) {
        this.eventRepository = eventRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public WorkflowEvent createEvent(WorkflowEvent event) {
        return eventRepository.save(event);
    }

    @Override
    public WorkflowEventType createEventType(WorkflowEventType eventType) {
        return eventTypeRepository.save(eventType);
    }

    @Override
    public Optional<WorkflowEventType> findEventType(String eventType) {
        return eventTypeRepository.findById(eventType);
    }

    @Override
    public WorkflowEvent createContinueEvent(WorkflowResult result) {
        WorkflowEvent event = new WorkflowEvent();
        event.setResult(result);
        event.setEventType(getContinueEventType());

        return eventRepository.save(event);
    }

    @Override
    public List<WorkflowEvent> getEventsSinceLastContinue(WorkflowResult result) {
        WorkflowEvent lastContinue = eventRepository.findTopByResultAndEventTypeOrderByDateCreatedDesc(result, getContinueEventType());
        Date lastEventCheckDate = lastContinue != null ? lastContinue.getDateCreated() : result.getDateCreated();

        return eventRepository.findAllByResultAndDateCreatedAfterOrderByDateCreated(result, lastEventCheckDate);
    }

    /**
     * Find or create the Continue Workflow event type.
     *
     * @return
     */
    private WorkflowEventType getContinueEventType() {
        WorkflowEventType continueEvent = findEventType(EventService.CONTINUE_EVENT).orElse(new WorkflowEventType());

        if (continueEvent.getEventType() == null) {
            continueEvent.setEventName("Continue Workflow");
            continueEvent.setEventType(EventService.CONTINUE_EVENT);
            eventTypeRepository.save(continueEvent);
        }

        return continueEvent;
    }
}

package gov.nysed.workflow.service.impl;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowEventType;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.domain.repository.WorkflowEventRepository;
import gov.nysed.workflow.domain.repository.WorkflowEventTypeRepository;
import gov.nysed.workflow.service.EventService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private WorkflowEventRepository workflowEventRepository;

    private WorkflowEventTypeRepository workflowEventTypeRepository;

    public EventServiceImpl(WorkflowEventRepository workflowEventRepository, WorkflowEventTypeRepository workflowEventTypeRepository) {
        this.workflowEventRepository = workflowEventRepository;
        this.workflowEventTypeRepository = workflowEventTypeRepository;
    }

    @Override
    public WorkflowEvent createEvent(WorkflowResult result, String eventType, String eventName) {
        WorkflowEvent event = new WorkflowEvent();
        event.setResult(result);
        event.setEventType(findEventType(eventType).orElse(createCompletedEventType(eventType, eventName)));

        return workflowEventRepository.save(event);
    }

    private WorkflowEventType createCompletedEventType(String eventType, String eventName) {
        WorkflowEventType eventTypeEntity = new WorkflowEventType();
        eventTypeEntity.setEventType(eventType);
        eventTypeEntity.setEventName("Attestation Completed");

        return workflowEventTypeRepository.save(eventTypeEntity);
    }

    @Override
    public WorkflowEventType createEventType(WorkflowEventType eventType) {
        return workflowEventTypeRepository.save(eventType);
    }

    @Override
    public Optional<WorkflowEventType> findEventType(String eventType) {
        return workflowEventTypeRepository.findById(eventType);
    }

    @Override
    public WorkflowEvent createContinueEvent(WorkflowResult result) {
        WorkflowEvent event = new WorkflowEvent();
        event.setResult(result);
        event.setEventType(getContinueEventType());

        return workflowEventRepository.save(event);
    }

    @Override
    public List<WorkflowEvent> getEventsSinceLastContinue(WorkflowResult result) {
        WorkflowEvent lastContinue = workflowEventRepository.findTopByResultAndEventTypeOrderByDateCreatedDesc(result, getContinueEventType());
        Date lastEventCheckDate = lastContinue != null ? lastContinue.getDateCreated() : result.getDateCreated();

        return workflowEventRepository.findAllByResultAndDateCreatedAfterOrderByDateCreated(result, lastEventCheckDate);
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
            workflowEventTypeRepository.save(continueEvent);
        }

        return continueEvent;
    }
}

package gov.nysed.workflow.processor;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.domain.entity.WorkflowConfig;
import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.domain.repository.ResultRepository;
import gov.nysed.workflow.domain.repository.WorkflowRepository;
import gov.nysed.workflow.exception.InvalidStepConfigException;
import gov.nysed.workflow.exception.WorkflowNotFoundException;
import gov.nysed.workflow.service.EventService;
import gov.nysed.workflow.util.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * This class runs a workflow based on a passed in form slug (from the URL) and runs through the step
 */
@Service
public class DefaultWorkflowProcessor {

    private WorkflowMapper workflowMapper;

    private ResultRepository resultRepository;

    private EventService eventService;

    private WorkflowRepository workflowRepository;

    public DefaultWorkflowProcessor(WorkflowMapper workflowMapper, ResultRepository resultRepository, EventService eventService, WorkflowRepository workflowRepository) {
        this.workflowMapper = workflowMapper;
        this.resultRepository = resultRepository;
        this.eventService = eventService;
        this.workflowRepository = workflowRepository;
    }

    public ModelAndView runWorkflow(String workflowSlug) {
        WorkflowConfig workflowConfig = workflowRepository.findWorkflowConfigBySlug(workflowSlug)
                .orElseThrow(() -> new WorkflowNotFoundException("Could not locate the workflow config for '" + workflowSlug + "'."));

        Workflow workflow =  workflowMapper.getWorkflow(workflowConfig)
                .orElseThrow(() -> new WorkflowNotFoundException("Could not locate the workflow class for '" + workflowSlug + "'."));

        WorkflowResult result = getWorkflowResult(workflowConfig);

        // if we are processing an existing step
        // do so before going on to the next
        processCurrentStep(workflow, result);


        // loop through steps and find the first one that is valid and not completed.
        for (Step step: workflow.getSteps()) {
            if (step.isStepValid(result) && !hasStepCompleted(result, step)) {
                ModelAndView view = step.runStep(result);
                setCurrentStep(result, step);
                return view;
            }
        }

        throw new InvalidStepConfigException("The last step in your config should either have no completed event or redirect out of the workflow.");
    }

    private void setCurrentStep(WorkflowResult result, Step step) {
        result.setCurrentStep(step.getName());
        resultRepository.save(result);
    }

    /**
     * Process submitted data (if current step was passed in the request)
     *
     * @param workflow
     * @param result
     */
    private void processCurrentStep(Workflow workflow, WorkflowResult result) {
        Step currentStep = workflow.getSteps()
                .stream()
                .filter(step -> RequestUtil.isCurrentStep(step.getName()))
                .findFirst()
                .orElse(null);

        if (currentStep != null) {
            currentStep.runStep(result);
        }
    }

    /**
     * Locate a WorkflowResult based on a UUID from the request or create a new one.
     *
     * @param workflowConfig
     * @return
     */
    private WorkflowResult getWorkflowResult(WorkflowConfig workflowConfig) {
        UUID requestId = RequestUtil.getResultUuid();
        WorkflowResult result = null;

        if (requestId != null) {
            result = resultRepository.findById(requestId).orElse(null);
        }

        if (result == null) {
            result = new WorkflowResult();
            result.setWorkflowConfig(workflowConfig);
            result = resultRepository.save(result);
        }

        return result;
    }

    /**
     * Determine if a step has already been completed for a result.
     *
     * @param result
     * @param step
     * @return
     */
    private boolean hasStepCompleted(WorkflowResult result, Step step) {
        for (WorkflowEvent event : eventService.getEventsSinceLastContinue(result)) {
            if (event.getEventType().getEventType().equals(step.getCompletedEventName())) {
                return true;
            }
        }

        return false;
    }
}

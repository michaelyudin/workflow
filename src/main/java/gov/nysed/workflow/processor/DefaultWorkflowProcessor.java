package gov.nysed.workflow.processor;

import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.domain.entity.WorkflowEvent;
import gov.nysed.workflow.exception.InvalidStepAccessException;
import gov.nysed.workflow.step.Step;
import gov.nysed.workflow.step.StepResult;
import gov.nysed.workflow.domain.entity.WorkflowIdentifier;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.domain.repository.WorkflowResultRepository;
import gov.nysed.workflow.domain.repository.WorkflowIdentifierRepository;
import gov.nysed.workflow.exception.InvalidStepConfigException;
import gov.nysed.workflow.exception.StepNotFoundException;
import gov.nysed.workflow.exception.WorkflowNotFoundException;
import gov.nysed.workflow.service.EventService;
import gov.nysed.workflow.step.WebStepResult;
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

    private WorkflowResultRepository workflowResultRepository;

    private EventService eventService;

    private WorkflowIdentifierRepository workflowIdentifierRepository;

    public DefaultWorkflowProcessor(WorkflowMapper workflowMapper, WorkflowResultRepository workflowResultRepository, EventService eventService, WorkflowIdentifierRepository workflowIdentifierRepository) {
        this.workflowMapper = workflowMapper;
        this.workflowResultRepository = workflowResultRepository;
        this.eventService = eventService;
        this.workflowIdentifierRepository = workflowIdentifierRepository;
    }

    public StepResult runWorkflow(String workflowSlug) {
        return runWorkflow(workflowSlug, null);
    }

    public StepResult runWorkflow(String workflowSlug, String jumpStep) {
        WorkflowIdentifier workflowIdentifier = workflowIdentifierRepository.findWorkflowConfigBySlug(workflowSlug)
                .orElseThrow(() -> new WorkflowNotFoundException("Could not locate the workflow config for '" + workflowSlug + "'."));

        Workflow workflow =  workflowMapper.getWorkflow(workflowIdentifier)
                .orElseThrow(() -> new WorkflowNotFoundException("Could not locate the workflow class for '" + workflowSlug + "'."));

        WorkflowResult result = getWorkflowResult(workflowIdentifier);

        jumpToStep(workflow, result, jumpStep);

        // if we are processing an existing step
        // do so before going on to the next
        StepResult stepResult = processCurrentStep(workflow, result);

        if (stepResult == null) {
            Step firstStep = workflow.getInitialStep(result);
            stepResult = firstStep.runStep(result);

            setCurrentStep(result, firstStep);
        }

        if (!stepResult.isComplete()) {
            return stepResult;
        } else {
            // attempt to get the next step based on the previous steps outputted event
            // if there is no event listener for that event, just get the next step in sequence.
            Step nextStep = workflow.getBuilder()
                    .getStepForEvent(stepResult.getEventName())
                    .orElse(getNextSequentialStep(workflow, result));

            setCurrentStep(result, nextStep);

            return new WebStepResult("REDIRECT", new ModelAndView("redirect:" + "/wf/" + workflowSlug + "/" + nextStep.getName() + "?rid=" + result.getId()), true);

        }
    }

    /**
     * Loop through each of the steps in a workflow until it finds one that is valid.
     *
     * @param workflow
     * @param result
     * @return
     */
    private Step getNextSequentialStep(Workflow workflow, WorkflowResult result) {
        Step nextStep;
        boolean isStepValid;

        String currentStep = result.getCurrentStep();

        do {
            // Attempt to find a step based on the last steps outputted event.
            // if it can't find a step for an event, find the next step in sequence.
            nextStep = workflow.getBuilder()
                    .getNextStepInSequence(currentStep)
                    .orElseThrow(() -> new InvalidStepConfigException("Could not locate a next step.  It's possible your last step does not terminate the process.  The last step should have no form or continue."));

            isStepValid = nextStep.isStepValid(result);

            if (!isStepValid) {
                currentStep = nextStep.getName();
            }

        } while (!isStepValid);

        return nextStep;
    }

    /**
     * If the terminal step has already been completed, this workflow cannot be run.
     *
     * @param workflow
     * @param result
     */
    private void checkIfInTerminalState(Workflow workflow, WorkflowResult result) {
        for (WorkflowEvent event : result.getEvents()) {
            if (event.getEventType().getEventType().equals(workflow.getTerminalEventName())) {
                throw new WorkflowNotFoundException("This workflow has already been completed.");
            }
        }
    }

    private void setCurrentStep(WorkflowResult result, Step step) {
        result.setCurrentStep(step.getName());
        workflowResultRepository.save(result);
    }

    /**
     * Process submitted data (if current step was passed in the request)
     *
     * @param workflow
     * @param result
     */
    private StepResult processCurrentStep(Workflow workflow, WorkflowResult result) {
        Step currentStep = workflow.getBuilder().getStep(result.getCurrentStep()).orElse(null);

        if (currentStep != null) {
            return currentStep.runStep(result);
        }

        return null;
    }

    /**
     * Locate a WorkflowResult based on a UUID from the request or create a new one.
     *
     * @param workflowIdentifier
     * @return
     */
    private WorkflowResult getWorkflowResult(WorkflowIdentifier workflowIdentifier) {
        UUID requestId = RequestUtil.getResultUuid();
        WorkflowResult result = null;

        if (requestId != null) {
            result = workflowResultRepository.findWorkflowResultById(requestId).orElse(null);
        }

        if (result == null) {
            result = new WorkflowResult();
            result.setWorkflowConfig(workflowIdentifier);
            result = workflowResultRepository.save(result);
        }

        return result;
    }

    /**
     * If user is trying to jump to a specific check, ensure they are able to do so
     * and set the result's current step to the requested step if allowed.
     *
     * @param workflow
     * @param result
     * @param step
     */
    private void jumpToStep(Workflow workflow, WorkflowResult result, String step) {
        if (step == null) {
            return;
        }

        Step currentStep = workflow.getBuilder()
                .getStep(step)
                .orElseThrow(() -> new StepNotFoundException("Could not locate step '" + step + "'."));

        if (!workflow.getTerminalEventName().equals(currentStep.getCompletedEventName())) {
            checkIfInTerminalState(workflow, result);
        }

        int currentIdx = workflow.getBuilder().getStepNames().indexOf(result.getCurrentStep());
        int requestedIdx = workflow.getBuilder().getStepNames().indexOf(step);

        if (requestedIdx > currentIdx) {
            throw new InvalidStepAccessException("CANT LOAD THIS STEP");
        }


        setCurrentStep(result, currentStep);
    }
}

package gov.nysed.workflow.processor;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.StepResult;
import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.domain.entity.WorkflowConfig;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import gov.nysed.workflow.domain.repository.ResultRepository;
import gov.nysed.workflow.domain.repository.WorkflowRepository;
import gov.nysed.workflow.exception.InvalidStepConfigException;
import gov.nysed.workflow.exception.StepNotFoundException;
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
        return runWorkflow(workflowSlug, null);
    }

    public ModelAndView runWorkflow(String workflowSlug, String jumpStep) {
        WorkflowConfig workflowConfig = workflowRepository.findWorkflowConfigBySlug(workflowSlug)
                .orElseThrow(() -> new WorkflowNotFoundException("Could not locate the workflow config for '" + workflowSlug + "'."));

        Workflow workflow =  workflowMapper.getWorkflow(workflowConfig)
                .orElseThrow(() -> new WorkflowNotFoundException("Could not locate the workflow class for '" + workflowSlug + "'."));

        WorkflowResult result = getWorkflowResult(workflowConfig);

        jumpToStep(workflow, result, jumpStep);

        // if we are processing an existing step
        // do so before going on to the next
        StepResult stepResult = processCurrentStep(workflow, result);

        if (stepResult == null) {
            Step firstStep = workflow.getInitialStep(result);
            stepResult = firstStep.runStep(result);

            setCurrentStep(result, firstStep);
        }

        if (stepResult.getOutput() != null) {
            return stepResult.getOutput();
        } else {
            Step nextStep = workflow.getBuilder()
                    .getStepForEvent(stepResult.getEventName())
                    .orElse(
                            workflow.getBuilder()
                                    .getNextStepInSequence(result.getCurrentStep())
                                    .orElse(null));

            if (nextStep == null) {
                throw new InvalidStepConfigException("Could not locate a next step.  It's possible your last step does not terminate the process.  The last step should have no form or continue.");
            }

            StepResult nextStepResult = nextStep.runStep(result);

            setCurrentStep(result, nextStep);

            return nextStepResult.getOutput();

        }
        //throw new InvalidStepConfigException("The last step in your config should either have no completed event or redirect out of the workflow.");
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
        int currentIdx = workflow.getBuilder().getStepNames().indexOf(result.getCurrentStep());
        int requestedIdx = workflow.getBuilder().getStepNames().indexOf(step);

        if (requestedIdx == -1 ) {
            throw new StepNotFoundException("Could not locate step '" + step + "'.");
        }

        if (requestedIdx > currentIdx) {
            throw new InvalidStepConfigException("CANT LOAD THIS STEP");
        }

        setCurrentStep(result, workflow.getBuilder().getStep(step).orElse(null));
    }
}

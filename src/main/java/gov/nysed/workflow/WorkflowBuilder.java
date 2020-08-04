package gov.nysed.workflow;

import java.util.*;

public class WorkflowBuilder {

    private Map<String, String> stepEventMap = new HashMap<>();

    public Map<String, Step> steps = new LinkedHashMap<>();

    public static WorkflowBuilder create() {
        return new WorkflowBuilder();
    }

    public WorkflowBuilder addStep(Step step) {
        this.steps.put(step.getName(), step);
        return this;
    }

    public WorkflowBuilder onEvent(String eventName, String stepName) {
        this.stepEventMap.put(eventName, stepName);
        return this;
    }

    public List<String> getStepNames() {
        return new LinkedList<>(steps.keySet());
    }

    public Optional<Step> getStep(String stepName) {
        return Optional.ofNullable(steps.get(stepName));
    }

    public Optional<Step> getStepForEvent(String eventName) {
        return getStep(stepEventMap.get(eventName));
    }

    public Map<String, Step> getSteps() {
        return steps;
    }

    public Optional<Step> getNextStepInSequence(String currentStep) {
        int currentStepIdx = this.getStepNames().indexOf(currentStep);

        return getStep(getStepNames().get(currentStepIdx + 1));
    }
}

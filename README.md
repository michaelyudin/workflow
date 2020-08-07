# Workflow Module

This module allows you to define workflows that run steps either sequentially or via events.

The [test](src/test/example) folder contains an example workflow with 3 steps.

## Building a Workflow

Each workflow should implement the [Workflow](src/main/java/gov/nysed/workflow/Workflow.java) interface:

```java
public class DemoWorkflow implements Workflow {

    private SomeStepDefinedAsAService serviceStep;

    public static String NAME = "demo-workflow";

    public DemoWorkflow(SomeStepDefinedAsAService serviceStep) {
        this.serviceStep = serviceStep;
    }

    @Override
    public WorkflowBuilder getBuilder() {
        CompletedStep completedStep = new CompletedStep();
        return WorkflowBuilder
                .create()
                .addStep(this.serviceStep)
                .addStep(new SomeOtherStepThatIsntAService())
                .addStep(new CompletedStep())
                .onEvent("STEP_ONE_COMPLETED", serviceStep.getName())
                .onEvent("STEP_TWO_COMPLETED", completedStep.getName());
    }

    @Override
    public String getName() {
        return NAME;
    }
}

```
**Note:** `onEvent()` in workflows is optional.  You can simply just use `addStep` if your workflow runs sequentially.  

Once a workflow is built, when your application runs, it will automatically insert your workflow into the database and is ready for use.  You can direct users to the /wf/{workflow-name} url to begin the workflow.

## Steps

A workflow is composed of steps.  Each step should output a [StepResult](src/main/java/gov/nysed/workflow/step/StepResult.java) object.  For Web workflows, you can return a [WebStepResult](src/main/java/gov/nysed/workflow/step/WebStepResult.java) object.

If a `StepResult` returns `isCompleted() == true`, it will attempt to load the next step.  Otherwise it will process the output of the `StepResult`.

Additionally, each `Step` should save an event that marks it as complete when it is finished processing.  For instance, after a Form step has been posted and saved, a "FORM_COMPLETED" event should be stored so the workflow knows that this step is completed.

```java
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
```

Each step can override the `boolean isStepValid(WorkflowResult result)` method.  If `isStepValid` returns false, the step will be skipped.
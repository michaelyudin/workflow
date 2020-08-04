package gov.nysed.workflow.step;

import org.springframework.web.servlet.ModelAndView;

public class WebStepResult implements StepResult<ModelAndView> {

    private String eventName;

    private ModelAndView output;

    private boolean isComplete;

    public WebStepResult(String eventName, ModelAndView output, boolean isComplete) {
        this.eventName = eventName;
        this.output = output;
        this.isComplete = isComplete;
    }

    @Override
    public ModelAndView getOutput() {
        return output;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}

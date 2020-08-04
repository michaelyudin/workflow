package gov.nysed.workflow.example.step;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.StepResult;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import org.springframework.web.servlet.ModelAndView;

public class ThankYouStep implements Step {

    @Override
    public StepResult runStep(WorkflowResult result) {
        ModelAndView view = new ModelAndView("thank-you");
        return new StepResult("THANK_YOU_LOADED", view);
    }

    @Override
    public String getName() {
        return "thank-you";
    }

    @Override
    public String getCompletedEventName() {
        return "THANK_YOU_LOADED";
    }
}

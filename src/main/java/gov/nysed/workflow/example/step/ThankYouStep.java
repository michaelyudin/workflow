package gov.nysed.workflow.example.step;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.domain.entity.WorkflowResult;
import org.springframework.web.servlet.ModelAndView;

public class ThankYouStep implements Step {
    @Override
    public ModelAndView runStep(WorkflowResult result) {
        return new ModelAndView("thank-you");
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

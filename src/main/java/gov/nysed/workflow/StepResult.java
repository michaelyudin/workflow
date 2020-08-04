package gov.nysed.workflow;

import lombok.Data;
import org.springframework.web.servlet.ModelAndView;

@Data
public class StepResult {

    private String eventName;

    private ModelAndView output;

    public StepResult(String eventName, ModelAndView output) {
        this.eventName = eventName;
        this.output = output;
    }
}

package gov.nysed.workflow.controller;

import gov.nysed.workflow.domain.repository.WorkflowRepository;
import gov.nysed.workflow.processor.DefaultWorkflowProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/wf/{workflow}")
public class WorkflowController {

    private DefaultWorkflowProcessor defaultWorkflowProcessor;

    private WorkflowRepository workflowRepository;

    public WorkflowController(DefaultWorkflowProcessor defaultWorkflowProcessor, WorkflowRepository workflowRepository) {
        this.defaultWorkflowProcessor = defaultWorkflowProcessor;
        this.workflowRepository = workflowRepository;
    }

    @RequestMapping()
    public ModelAndView process(@PathVariable(name="workflow") String workflow) {
        ModelAndView view = defaultWorkflowProcessor.runWorkflow(workflow);
        view.addObject("workflowSlug", workflow);

        return view;
    }

}

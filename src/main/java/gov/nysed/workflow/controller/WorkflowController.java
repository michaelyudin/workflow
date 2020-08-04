package gov.nysed.workflow.controller;

import gov.nysed.workflow.domain.repository.WorkflowRepository;
import gov.nysed.workflow.processor.DefaultWorkflowProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/wf/{workflow}")
public class WorkflowController {

    private DefaultWorkflowProcessor defaultWorkflowProcessor;

    public WorkflowController(DefaultWorkflowProcessor defaultWorkflowProcessor) {
        this.defaultWorkflowProcessor = defaultWorkflowProcessor;
    }

    @RequestMapping()
    public ModelAndView process(@PathVariable(name="workflow") String workflow, HttpServletResponse response) {
        ModelAndView view = defaultWorkflowProcessor.runWorkflow(workflow);
        view.addObject("workflowSlug", workflow);

        preventHttpCache(response);

        return view;
    }

    @RequestMapping("/{step}")
    public ModelAndView processStep(
            @PathVariable(name="workflow") String workflow,
            @PathVariable(name="step") String step,
            HttpServletResponse response
    ) {
        ModelAndView view = defaultWorkflowProcessor.runWorkflow(workflow, step);
        view.addObject("workflowSlug", workflow);

        preventHttpCache(response);
        return view;
    }

    /**
     * This forces the browser to reload the controller instead of loading pages from HTTP cache.
     * This is so when users press the back button the workflow process runs.
     *
     * @param response
     */
    private void preventHttpCache(HttpServletResponse response) {
        response.setHeader("pragma", "no-cache");
        response.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
        response.setHeader("Expires", "0");
    }

}

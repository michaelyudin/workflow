package gov.nysed.workflow.example;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.example.step.AttestationStep;
import gov.nysed.workflow.example.step.ResultStep;
import gov.nysed.workflow.example.step.ThankYouStep;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataWorkflow implements Workflow {

    private ResultStep resultStep;

    private AttestationStep attestationStep;

    public DataWorkflow(ResultStep resultStep, AttestationStep attestationStep) {
        this.resultStep = resultStep;
        this.attestationStep = attestationStep;
    }

    @Override
    public List<Step> getSteps() {
        return Arrays.asList(
                resultStep,
                attestationStep,
                new ThankYouStep()
        );
    }

    @Override
    public String getName() {
        return "replace-reg";
    }
}

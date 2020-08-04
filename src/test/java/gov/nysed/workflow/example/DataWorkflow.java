package gov.nysed.workflow.example;

import gov.nysed.workflow.Step;
import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.WorkflowBuilder;
import gov.nysed.workflow.example.step.AttestationStep;
import gov.nysed.workflow.example.step.DataServiceFormStep;
import gov.nysed.workflow.example.step.ThankYouStep;
import org.springframework.stereotype.Service;

@Service
public class DataWorkflow implements Workflow {

    private DataServiceFormStep resultStep;

    private AttestationStep attestationStep;

    public static String NAME = "replace-reg";

    public DataWorkflow(DataServiceFormStep dataServiceFormStep, AttestationStep attestationStep) {
        this.resultStep = dataServiceFormStep;
        this.attestationStep = attestationStep;
    }

    @Override
    public WorkflowBuilder getBuilder() {
        Step thankYouStep = new ThankYouStep();

        return WorkflowBuilder
                .create()
                .addStep(this.resultStep)
                .addStep(this.attestationStep)
                .addStep(thankYouStep)
                .onEvent("RESULT_COMPLETED", attestationStep.getName())
                .onEvent("ATTESTATION_COMPLETED", thankYouStep.getName());
    }

    @Override
    public String getName() {
        return NAME;
    }

}

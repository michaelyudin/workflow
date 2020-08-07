package gov.nysed.workflow.example;

import gov.nysed.workflow.Workflow;
import gov.nysed.workflow.WorkflowBuilder;
import gov.nysed.workflow.example.step.AttestationStep;
import gov.nysed.workflow.example.step.DataServiceFormStep;
import gov.nysed.workflow.example.step.ThankYouStep;
import org.springframework.stereotype.Service;

@Service
public class ReplacementRegWebWorkflow implements Workflow {

    private DataServiceFormStep dataServiceFormStep;

    private AttestationStep attestationStep;

    private ThankYouStep thankYouStep;

    public static String NAME = "replace-reg";

    public ReplacementRegWebWorkflow(DataServiceFormStep dataServiceFormStep, AttestationStep attestationStep, ThankYouStep thankYouStep) {
        this.dataServiceFormStep = dataServiceFormStep;
        this.attestationStep = attestationStep;
        this.thankYouStep = thankYouStep;
    }

    @Override
    public WorkflowBuilder getBuilder() {

        return WorkflowBuilder
                .create()
                .addStep(this.dataServiceFormStep)
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

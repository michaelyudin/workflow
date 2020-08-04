package gov.nysed.workflow.example.domain;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "ATTESTATION_EVENT")
public class AttestationEvent {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private WorkflowEvent event;

    @NotBlank
    private String signature;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WorkflowEvent getEvent() {
        return event;
    }

    public void setEvent(WorkflowEvent event) {
        this.event = event;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

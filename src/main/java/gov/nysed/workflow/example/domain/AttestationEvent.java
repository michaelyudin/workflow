package gov.nysed.workflow.example.domain;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "ATTESTATION_EVENT")
@Data
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
}

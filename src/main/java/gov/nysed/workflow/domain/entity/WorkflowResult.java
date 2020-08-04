package gov.nysed.workflow.domain.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Stores a record of a workflow created by and end-user.
 */
@Entity
@Table(name="WORKFLOW_RESULT")
@Data
public class WorkflowResult {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private WorkflowConfig workflowConfig;

    @OneToMany(targetEntity = WorkflowEvent.class, mappedBy = "result")
    @OrderBy("dateCreated")
    private List<WorkflowEvent> events = new LinkedList<>();

    @Column
    private String currentStep;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateCreated;
}

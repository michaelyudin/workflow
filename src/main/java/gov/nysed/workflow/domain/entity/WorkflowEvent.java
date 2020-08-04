package gov.nysed.workflow.domain.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Stores events for a specific workflow result.
 */
@Entity
@Table(name = "WORKFLOW_EVENT")
public class WorkflowEvent {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private WorkflowResult result;

    @ManyToOne
    private WorkflowEventType eventType;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateCreated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public WorkflowResult getResult() {
        return result;
    }

    public void setResult(WorkflowResult result) {
        this.result = result;
    }

    public WorkflowEventType getEventType() {
        return eventType;
    }

    public void setEventType(WorkflowEventType eventType) {
        this.eventType = eventType;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

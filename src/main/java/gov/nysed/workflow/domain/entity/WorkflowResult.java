package gov.nysed.workflow.domain.entity;

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
    private WorkflowIdentifier workflowIdentifier;

    @OneToMany(targetEntity = WorkflowEvent.class, mappedBy = "result", fetch = FetchType.EAGER)
    @OrderBy("dateCreated")
    private List<WorkflowEvent> events = new LinkedList<>();

    @Column
    private String currentStep;

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

    public WorkflowIdentifier getWorkflowConfig() {
        return workflowIdentifier;
    }

    public void setWorkflowConfig(WorkflowIdentifier workflowIdentifier) {
        this.workflowIdentifier = workflowIdentifier;
    }

    public List<WorkflowEvent> getEvents() {
        return events;
    }

    public void setEvents(List<WorkflowEvent> events) {
        this.events = events;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

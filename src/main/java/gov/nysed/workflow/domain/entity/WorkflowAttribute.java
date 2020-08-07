package gov.nysed.workflow.domain.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Stores attributes/configuration/options for a workflow.
 */
@Entity
@Table(name = "WORKFLOW_ATTRIBUTE")
public class WorkflowAttribute {

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

    private String key;

    private String value;

    public UUID getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public WorkflowIdentifier getWorkflowIdentifier() {
        return workflowIdentifier;
    }

    public void setWorkflowIdentifier(WorkflowIdentifier workflowIdentifier) {
        this.workflowIdentifier = workflowIdentifier;
    }
}

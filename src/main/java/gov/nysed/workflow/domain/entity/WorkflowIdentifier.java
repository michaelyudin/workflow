package gov.nysed.workflow.domain.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "WORKFLOW_IDENTIFIER")
public class WorkflowIdentifier {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String slug;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateCreated;

    @OneToMany(targetEntity = WorkflowAttribute.class, mappedBy = "workflowIdentifier")
    private List<WorkflowAttribute> attributes = new LinkedList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public List<WorkflowAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<WorkflowAttribute> attributes) {
        this.attributes = attributes;
    }
}

package gov.nysed.workflow.example.domain;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="DATA_SERVICE_RESULT")
public class DataServiceResult {

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

    private String dataServiceResultId;

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

    public String getDataServiceResultId() {
        return dataServiceResultId;
    }

    public void setDataServiceResultId(String dataServiceResultId) {
        this.dataServiceResultId = dataServiceResultId;
    }
}

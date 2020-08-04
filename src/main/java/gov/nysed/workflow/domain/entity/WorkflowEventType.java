package gov.nysed.workflow.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WORKFLOW_EVENT_TYPE")
@Data
public class WorkflowEventType {

    @Id
    private String eventType;

    @Column
    private String eventName;
}

package gov.nysed.workflow.example.domain;

import gov.nysed.workflow.domain.entity.WorkflowEvent;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="DATA_SERVICE_RESULT")
@Data
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


}

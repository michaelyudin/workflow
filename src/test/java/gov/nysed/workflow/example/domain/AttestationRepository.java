package gov.nysed.workflow.example.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttestationRepository extends JpaRepository<AttestationEvent, UUID> {
}

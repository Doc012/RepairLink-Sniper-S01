package com.backend.app.serviceHistories.repositories;



import com.backend.app.serviceHistories.entities.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {
}

package com.backend.serviceHistories.repositories;



import com.backend.serviceHistories.entities.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {
}

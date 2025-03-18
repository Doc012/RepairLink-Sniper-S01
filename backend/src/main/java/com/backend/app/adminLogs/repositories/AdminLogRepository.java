package com.backend.app.adminLogs.repositories;

import com.backend.app.adminLogs.entities.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {
}

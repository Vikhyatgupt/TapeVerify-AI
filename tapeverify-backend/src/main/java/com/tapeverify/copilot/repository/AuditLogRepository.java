package com.tapeverify.copilot.repository;
import com.tapeverify.copilot.entity.AuditLog;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> { List<AuditLog> findByLoanIdOrderByTimestampAsc(Long loanId); }

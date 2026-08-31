package com.tapeverify.copilot.repository;
import com.tapeverify.copilot.entity.ValidationException;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ValidationExceptionRepository extends JpaRepository<ValidationException, Long> { List<ValidationException> findByResolvedFalse(); List<ValidationException> findByLoanIdAndResolvedFalse(Long loanId); org.springframework.data.domain.Page<ValidationException> findByResolvedFalse(org.springframework.data.domain.Pageable pageable); List<ValidationException> findByResolvedFalseAndSeverityIgnoreCase(String severity); List<ValidationException> findByResolvedFalseAndFieldNameIgnoreCase(String fieldName); }

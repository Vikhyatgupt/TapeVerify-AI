package com.tapeverify.copilot.repository;
import com.tapeverify.copilot.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LoanRepository extends JpaRepository<Loan, Long> { List<Loan> findByBatchId(String batchId); List<Loan> findByStatus(LoanStatus status); long countByStatus(LoanStatus status); List<Loan> findBySourceLoanId(String sourceLoanId); List<Loan> findByBorrowerNameAndPrincipalAmountAndOriginationDate(String borrowerName, java.math.BigDecimal principalAmount, java.time.LocalDate originationDate); }

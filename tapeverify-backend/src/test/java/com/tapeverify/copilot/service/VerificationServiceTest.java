package com.tapeverify.copilot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tapeverify.copilot.entity.*;
import com.tapeverify.copilot.repository.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class VerificationServiceTest {
  private LoanRepository loans; private VerifiedLoanRepository verified; private ValidationExceptionRepository exceptions; private AuditService audit; private VerificationService service;
  @BeforeEach void setup() {
    loans = mock(LoanRepository.class); verified = mock(VerifiedLoanRepository.class); exceptions = mock(ValidationExceptionRepository.class); audit = mock(AuditService.class);
    service = new VerificationService(loans, verified, mock(ReviewerActionRepository.class), exceptions, audit, new ObjectMapper().findAndRegisterModules());
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("reviewer", "n/a"));
  }
  @AfterEach void clear() { SecurityContextHolder.clearContext(); }
  @Test void approvalCreatesAVerifiedRecordOnlyWhenNoExceptionsRemain() {
    Loan loan = Loan.builder().id(1L).batchId("b").sourceLoanId("LN-1").borrowerName("Ada").principalAmount(new BigDecimal("100")).currentBalance(new BigDecimal("90")).interestRate(new BigDecimal("5")).originationDate(LocalDate.of(2024,1,1)).maturityDate(LocalDate.of(2025,1,1)).paymentStatus("CURRENT").documentStatus("AVAILABLE").status(LoanStatus.RESOLVED).sha256Hash("old").createdAt(LocalDateTime.now()).build();
    when(loans.findById(1L)).thenReturn(Optional.of(loan)); when(exceptions.findByLoanIdAndResolvedFalse(1L)).thenReturn(List.of()); when(verified.findByLoanId(1L)).thenReturn(Optional.empty());
    when(verified.save(any())).thenAnswer(invocation -> { VerifiedLoan value = invocation.getArgument(0); value.setId(9L); return value; });

    VerifiedLoan result = service.decide(1L, null, "APPROVE", "checked");

    assertEquals(9L, result.getId()); assertEquals("reviewer", result.getVerifiedBy()); assertEquals(LoanStatus.VERIFIED, loan.getStatus());
    verify(audit, times(2)).record(eq(loan), any(), eq("reviewer"), isNull(), isNull());
  }

  @Test void rejectsReviewActionsThatReferenceAnotherLoansException() {
    Loan requestedLoan = Loan.builder().id(1L).batchId("b").status(LoanStatus.EXCEPTION).sha256Hash("hash").createdAt(LocalDateTime.now()).build();
    Loan otherLoan = Loan.builder().id(2L).batchId("b").status(LoanStatus.EXCEPTION).sha256Hash("hash").createdAt(LocalDateTime.now()).build();
    ValidationException exception = ValidationException.builder().id(10L).loan(otherLoan).fieldName("principalAmount").errorMessage("Invalid").resolved(false).build();
    when(loans.findById(1L)).thenReturn(Optional.of(requestedLoan));
    when(exceptions.findById(10L)).thenReturn(Optional.of(exception));

    IllegalArgumentException result = assertThrows(IllegalArgumentException.class,
        () -> service.decide(1L, 10L, "COMMENT", "review"));

    assertEquals("Exception does not belong to this loan", result.getMessage());
  }
}

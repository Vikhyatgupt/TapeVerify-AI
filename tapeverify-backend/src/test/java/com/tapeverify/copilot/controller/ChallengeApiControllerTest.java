package com.tapeverify.copilot.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tapeverify.copilot.dto.LoanDto;
import com.tapeverify.copilot.entity.*;
import com.tapeverify.copilot.repository.ValidationExceptionRepository;
import com.tapeverify.copilot.service.LoanService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ChallengeApiControllerTest {
  @Test void filtersAndPaginatesOpenExceptionsOnTheServer() {
    ValidationExceptionRepository exceptions = mock(ValidationExceptionRepository.class);
    LoanService loans = mock(LoanService.class);
    ChallengeApiController controller = new ChallengeApiController(exceptions, loans);
    Loan loan = Loan.builder().id(7L).batchId("b").sourceLoanId("LN-7").borrowerId("BR-7").borrowerName("Ada").status(LoanStatus.EXCEPTION).sha256Hash("x").createdAt(LocalDateTime.now()).build();
    ValidationException issue = ValidationException.builder().id(3L).loan(loan).fieldName("principalAmount").severity("HIGH").errorMessage("Invalid amount").resolved(false).build();
    when(exceptions.findByResolvedFalse()).thenReturn(List.of(issue));
    when(loans.find(7L)).thenReturn(LoanDto.builder().id(7L).sourceLoanId("LN-7").build());

    Map<String, Object> response = controller.exceptionQueue(0, 20, "HIGH", null, "ln-7");

    assertEquals(1, response.get("total"));
    assertEquals(1, ((List<?>) response.get("items")).size());
    verify(loans).find(7L);
  }
}

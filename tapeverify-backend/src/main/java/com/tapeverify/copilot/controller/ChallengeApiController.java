package com.tapeverify.copilot.controller;

import com.tapeverify.copilot.dto.*;
import com.tapeverify.copilot.entity.ValidationException;
import com.tapeverify.copilot.repository.*;
import com.tapeverify.copilot.service.*;
import java.util.*;
import java.util.stream.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

/** Competition-friendly aliases and a server-side exception queue. */
@RestController
@RequestMapping("/api/v1")
public class ChallengeApiController {
  private final ValidationExceptionRepository exceptions;
  private final LoanService loans;

  public ChallengeApiController(ValidationExceptionRepository exceptions, LoanService loans) {
    this.exceptions = exceptions; this.loans = loans;
  }

  @GetMapping("/exceptions")
  public Map<String, Object> exceptionQueue(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "25") int size,
      @RequestParam(required = false) String severity,
      @RequestParam(required = false) String field,
      @RequestParam(required = false) String query) {
    int safePage = Math.max(0, page), safeSize = Math.min(100, Math.max(1, size));
    Stream<ValidationException> stream = openExceptions(severity, field).stream();
    String needle = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
    if (!needle.isBlank()) stream = stream.filter(e -> contains(e.getLoan().getSourceLoanId(), needle)
        || contains(e.getLoan().getBorrowerId(), needle) || contains(e.getLoan().getBorrowerName(), needle));
    List<ValidationException> filtered = stream.sorted(Comparator.comparing(ValidationException::getId)).toList();
    List<Map<String, Object>> items = filtered.stream().skip((long) safePage * safeSize).limit(safeSize).map(e -> {
      Map<String, Object> item = new LinkedHashMap<>(); item.put("exception", exceptionDto(e)); item.put("loan", loans.find(e.getLoan().getId())); return item;
    }).toList();
    return Map.of("items", items, "page", safePage, "size", safeSize, "total", filtered.size(),
        "totalPages", (int) Math.ceil((double) filtered.size() / safeSize));
  }

  private List<ValidationException> openExceptions(String severity, String field) {
    Stream<ValidationException> stream = exceptions.findByResolvedFalse().stream();
    if (severity != null && !severity.isBlank()) stream = stream.filter(e -> severity.trim().equalsIgnoreCase(e.getSeverity()));
    if (field != null && !field.isBlank()) stream = stream.filter(e -> field.trim().equalsIgnoreCase(e.getFieldName()));
    return stream.toList();
  }
  private boolean contains(String value, String needle) { return value != null && value.toLowerCase(Locale.ROOT).contains(needle); }
  private ExceptionDto exceptionDto(ValidationException e) { return ExceptionDto.builder().id(e.getId()).fieldName(e.getFieldName()).errorMessage(e.getErrorMessage()).severity(e.getSeverity()).rowNumber(e.getRowNumber()).rawValue(e.getRawValue()).normalizedValue(e.getNormalizedValue()).aiExplanation(e.getAiExplanation()).aiSuggestedValue(e.getAiSuggestedValue()).aiConfidence(e.getAiConfidence()).aiReasoningSummary(e.getAiReasoningSummary()).resolved(e.isResolved()).build(); }
}

package com.tapeverify.copilot.service;
import com.tapeverify.copilot.dto.*; import com.tapeverify.copilot.entity.*; import java.util.List;
public interface AuditService { void record(Loan loan, AuditAction action, String user, String oldHash, AuditLoanSnapshotDto oldDetails); List<AuditLogDto> history(Long loanId); }

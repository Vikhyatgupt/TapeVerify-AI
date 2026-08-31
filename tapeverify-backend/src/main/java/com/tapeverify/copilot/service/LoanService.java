package com.tapeverify.copilot.service;
import com.tapeverify.copilot.dto.*; import java.util.List;
public interface LoanService { List<LoanDto> all(); List<LoanDto> exceptions(); LoanDto find(Long id); LoanDto resolve(Long id, ResolveDto dto); }

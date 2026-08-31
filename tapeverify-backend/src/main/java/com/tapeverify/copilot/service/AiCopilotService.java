package com.tapeverify.copilot.service;
import com.tapeverify.copilot.dto.AiSuggestionDto;
public interface AiCopilotService { AiSuggestionDto explain(Long loanId, Long exceptionId); }

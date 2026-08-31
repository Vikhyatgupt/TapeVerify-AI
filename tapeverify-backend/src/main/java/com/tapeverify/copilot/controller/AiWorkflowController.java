package com.tapeverify.copilot.controller;
import com.tapeverify.copilot.repository.ValidationExceptionRepository; import java.util.*; import org.springframework.web.bind.annotation.*;
/** Safe, deterministic AI-workflow helpers for batch triage and human-reviewed rule authoring. */
@RestController @RequestMapping("/api/v1/ai") public class AiWorkflowController {
 private final ValidationExceptionRepository exceptions; public AiWorkflowController(ValidationExceptionRepository exceptions){this.exceptions=exceptions;}
 @GetMapping("/exceptions/summary") public Map<String,Object> summary(){Map<String,Long> byField=new TreeMap<>(),bySeverity=new TreeMap<>();exceptions.findByResolvedFalse().forEach(e->{byField.merge(e.getFieldName(),1L,Long::sum);bySeverity.merge(e.getSeverity(),1L,Long::sum);});return Map.of("openExceptionCount",exceptions.findByResolvedFalse().size(),"byField",byField,"bySeverity",bySeverity,"recommendation","Prioritize HIGH severity exceptions, then resolve the largest repeated field category.");}
 @PostMapping("/rules/draft") public Map<String,Object> draft(@RequestBody Map<String,String> input){String request=Optional.ofNullable(input.get("request")).orElse("").trim();if(request.isBlank())throw new IllegalArgumentException("Describe the validation rule to draft");return Map.of("request",request,"draftRule",Map.of("name","human_review_required","description",request,"severity","MEDIUM","testCase","Reject a record that violates this rule"),"status","DRAFT_ONLY","notice","A reviewer must approve this draft before it is added to validation_rules.json.");}
}

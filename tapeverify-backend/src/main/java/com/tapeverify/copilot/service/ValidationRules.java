package com.tapeverify.copilot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Component;

/** Challenge rules are externalized so a new rule file can be reviewed without changing validation code. */
@Component public class ValidationRules {
  private final ObjectMapper json; private Map<String,Object> config = Map.of();
  public ValidationRules(ObjectMapper json) { this.json=json; }
  @PostConstruct void load() { try (InputStream in=getClass().getResourceAsStream("/validation_rules.json")) { config=json.readValue(in,new TypeReference<>(){}); } catch(Exception e) { throw new IllegalStateException("Cannot load validation_rules.json",e); } }
  @SuppressWarnings("unchecked") private Map<String,Object> map(String key){ return (Map<String,Object>)config.get(key); }
  public BigDecimal minRate(){return new BigDecimal(map("interestRate").get("min").toString());} public BigDecimal maxRate(){return new BigDecimal(map("interestRate").get("max").toString());} public int staleDays(){return ((Number)config.get("staleAfterDays")).intValue();}
  @SuppressWarnings("unchecked") public Set<String> paymentStatuses(){return new HashSet<>((List<String>)config.get("validPaymentStatuses"));} @SuppressWarnings("unchecked") public Set<String> documentStatuses(){return new HashSet<>((List<String>)config.get("availableDocumentStatuses"));}
}

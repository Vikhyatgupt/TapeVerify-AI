package com.tapeverify.copilot.util;

import com.tapeverify.copilot.exception.FileProcessingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.math.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import org.apache.commons.csv.*;

/** Parses each row independently so malformed data becomes an exception, not a failed batch. */
public final class CsvParserUtil {
  private CsvParserUtil() {}
  private static final Map<String,List<String>> ALIASES = Map.ofEntries(
    Map.entry("sourceLoanId", List.of("loan_id","loanid","id")), Map.entry("borrowerId", List.of("borrower_id","borrowerid")),
    Map.entry("borrowerName", List.of("borrower","borrower_name","borrowername")), Map.entry("principalAmount", List.of("principal","principal_amount","principalamount","original_principal")),
    Map.entry("currentBalance", List.of("current_balance","balance")), Map.entry("interestRate", List.of("rate","interest_rate","interestrate")),
    Map.entry("originationDate", List.of("origination_date","originationdate","origination")), Map.entry("maturityDate", List.of("maturity_date","maturitydate","maturity")),
    Map.entry("paymentStatus", List.of("payment_status")), Map.entry("daysPastDue", List.of("days_past_due","dpd")), Map.entry("borrowerState", List.of("borrower_state","state")),
    Map.entry("documentStatus", List.of("document_status","document_availability")), Map.entry("lastUpdatedAt", List.of("last_updated_at","last_updated")),
    Map.entry("sourceSystem", List.of("source_system","source")), Map.entry("loanType", List.of("loan_type")), Map.entry("termMonths", List.of("term_months","term")),
    Map.entry("loanPurpose", List.of("loan_purpose","purpose")), Map.entry("creditGrade", List.of("credit_grade")), Map.entry("employmentLength", List.of("employment_length")),
    Map.entry("incomeBand", List.of("income_band")), Map.entry("lastPaymentDate", List.of("last_payment_date")));

  public static List<LoanRow> parse(InputStream input) {
    try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8); CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setTrim(false).build().parse(reader)) {
      List<LoanRow> rows = new ArrayList<>(); for (CSVRecord record : parser) rows.add(row(record)); return rows;
    } catch (Exception e) { throw new FileProcessingException("Unable to read CSV. Check the header and CSV quoting.", e); }
  }
  private static LoanRow row(CSVRecord r) {
    Map<String,String> raw = new LinkedHashMap<>();
    for (String field : ALIASES.keySet()) raw.put(field, value(r, field));
    for (String header : r.getParser().getHeaderMap().keySet()) raw.putIfAbsent(header, r.get(header));
    List<ParseIssue> issues = new ArrayList<>();
    return new LoanRow((int) r.getRecordNumber()+1, n(raw.get("sourceLoanId")), n(raw.get("borrowerId")), n(raw.get("borrowerName")),
      decimal(raw.get("principalAmount"),"principalAmount",issues), decimal(raw.get("currentBalance"),"currentBalance",issues), decimal(raw.get("interestRate"),"interestRate",issues),
      date(raw.get("originationDate"),"originationDate",issues), date(raw.get("maturityDate"),"maturityDate",issues), n(raw.get("paymentStatus")), integer(raw.get("daysPastDue"),"daysPastDue",issues), n(raw.get("borrowerState")), n(raw.get("documentStatus")),
      timestamp(raw.get("lastUpdatedAt"),"lastUpdatedAt",issues), n(raw.get("sourceSystem")), n(raw.get("loanType")), integer(raw.get("termMonths"),"termMonths",issues), n(raw.get("loanPurpose")), n(raw.get("creditGrade")), n(raw.get("employmentLength")), n(raw.get("incomeBand")), date(raw.get("lastPaymentDate"),"lastPaymentDate",issues), raw, issues);
  }
  private static String value(CSVRecord r, String field) { for(String alias:ALIASES.get(field)) for(String header:r.getParser().getHeaderMap().keySet()) if(normalizeHeader(header).equals(alias)) return r.get(header); return null; }
  private static String normalizeHeader(String value) { return value.trim().toLowerCase(Locale.ROOT).replace(" ","_"); }
  private static String n(String value) { return value==null?null:value.trim().replaceAll("\\s+"," "); }
  private static BigDecimal decimal(String raw,String field,List<ParseIssue> issues) { if(raw==null||raw.isBlank()) return null; try { return new BigDecimal(raw.trim().replaceAll("[,$₹\\s]", "").replace("%","")).setScale(2,RoundingMode.HALF_UP); } catch(Exception e) { issues.add(new ParseIssue(field,"Malformed amount",raw)); return null; } }
  private static Integer integer(String raw,String field,List<ParseIssue> issues) { if(raw==null||raw.isBlank()) return null; try { return Integer.valueOf(raw.trim()); } catch(Exception e) { issues.add(new ParseIssue(field,"Malformed number",raw)); return null; } }
  private static LocalDateTime timestamp(String raw,String field,List<ParseIssue> issues) { if(raw==null||raw.isBlank()) return null; try { return LocalDateTime.parse(raw.trim().replace("Z", "")); } catch(Exception e) { try { return OffsetDateTime.parse(raw.trim()).toLocalDateTime(); } catch(Exception ignored) { issues.add(new ParseIssue(field,"Malformed timestamp",raw)); return null; } } }
  private static LocalDate date(String raw,String field,List<ParseIssue> issues) { if(raw==null||raw.isBlank()) return null; for(DateTimeFormatter f:List.of(DateTimeFormatter.ISO_LOCAL_DATE,DateTimeFormatter.ofPattern("M/d/uuuu"),DateTimeFormatter.ofPattern("MM-dd-uuuu"),DateTimeFormatter.ofPattern("dd-MMM-uuuu",Locale.ENGLISH))) try{return LocalDate.parse(raw.trim(),f);}catch(Exception ignored){} issues.add(new ParseIssue(field,"Malformed date",raw)); return null; }
  public record LoanRow(int rowNumber,String sourceLoanId,String borrowerId,String borrowerName,BigDecimal principalAmount,BigDecimal currentBalance,BigDecimal interestRate,LocalDate originationDate,LocalDate maturityDate,String paymentStatus,Integer daysPastDue,String borrowerState,String documentStatus,LocalDateTime lastUpdatedAt,String sourceSystem,String loanType,Integer termMonths,String loanPurpose,String creditGrade,String employmentLength,String incomeBand,LocalDate lastPaymentDate,Map<String,String> rawValues,List<ParseIssue> parseIssues) {}
  public record ParseIssue(String fieldName,String message,String rawValue) {}
}

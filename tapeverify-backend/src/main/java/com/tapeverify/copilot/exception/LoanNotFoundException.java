package com.tapeverify.copilot.exception;
public class LoanNotFoundException extends RuntimeException { public LoanNotFoundException(Long id) { super("Loan " + id + " was not found"); } }

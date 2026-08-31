package com.tapeverify.copilot.service;
import com.tapeverify.copilot.entity.*; import java.util.List;
public interface ValidationService { List<ValidationException> validate(Loan loan); }

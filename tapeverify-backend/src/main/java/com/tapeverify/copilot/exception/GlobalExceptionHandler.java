package com.tapeverify.copilot.exception;
import java.util.Map; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException; import org.springframework.web.bind.MethodArgumentNotValidException;
@RestControllerAdvice public class GlobalExceptionHandler {
 @ExceptionHandler(LoanNotFoundException.class) ResponseEntity<Map<String,String>> notFound(RuntimeException e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",e.getMessage())); }
 @ExceptionHandler({FileProcessingException.class, IllegalArgumentException.class}) ResponseEntity<Map<String,String>> badRequest(RuntimeException e) { return ResponseEntity.badRequest().body(Map.of("message",e.getMessage())); }
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<Map<String,String>> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(Map.of("message",e.getReason()==null?"Request failed":e.getReason()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("message",e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+" "+x.getDefaultMessage()).findFirst().orElse("Invalid request")));}
 @ExceptionHandler(Exception.class) ResponseEntity<Map<String,String>> unexpected(Exception e) { return ResponseEntity.status(500).body(Map.of("message","An unexpected error occurred")); }
}

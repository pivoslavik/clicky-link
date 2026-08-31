package ru.clicky.link.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clicky.link.common.response.MessageResponse;
import ru.clicky.link.core.LinkAlreadyExistsException;
import ru.clicky.link.core.LinkNotFoundException;
import ru.clicky.link.ratelimiter.RateLimitExceededException;

@RestControllerAdvice
public class RestExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(LinkAlreadyExistsException.class)
  public ResponseEntity<MessageResponse> handleBadRequestException(RuntimeException e) {
    logger.warn(e.getMessage(), e);
    return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
  }

  @ExceptionHandler(LinkNotFoundException.class)
  public ResponseEntity<MessageResponse> handleNotFoundException(RuntimeException e) {
    logger.warn(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<MessageResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    logger.warn(e.getMessage(), e);
    FieldError firstError = e.getBindingResult().getFieldError();
    String defaultMessage = (firstError != null) ? firstError.getDefaultMessage() : "Validation Error";
    return ResponseEntity.badRequest().body(new MessageResponse(defaultMessage));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<MessageResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    logger.warn(e.getMessage(), e);
    return ResponseEntity.badRequest().body(new MessageResponse("Error processing the database request. Please try again later"));
  }

  @ExceptionHandler(RateLimitExceededException.class)
  public ResponseEntity<MessageResponse> handleRateLimit(RateLimitExceededException e) {
    logger.warn(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new MessageResponse(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<MessageResponse> handleException(Exception e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.internalServerError().body(new MessageResponse("An internal server error occurred. Please try again later"));
  }
}

package com.example.demo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class InvalidDataException extends BusinessException {

  public InvalidDataException() {
    super();
  }

  public InvalidDataException(String message) {
    super(message);
  }

  public InvalidDataException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidDataException(Throwable cause) {
    super(cause);
  }

  protected InvalidDataException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

package ru.clicky.link.core;

public class LinkAlreadyExistsException extends RuntimeException {
  public LinkAlreadyExistsException(String message) {
    super(message);
  }
}

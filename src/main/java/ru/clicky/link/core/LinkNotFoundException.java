package ru.clicky.link.core;

public class LinkNotFoundException extends RuntimeException {
  public LinkNotFoundException(String message) {
    super(message);
  }
}

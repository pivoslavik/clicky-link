package ru.clicky.link.core;

public final class LinkMapper {
  public static LinkInfo toInfo(Link link) {
    if (link == null) {
      return null;
    }
    return new LinkInfo(link.getShortUrl(), link.getOriginalUrl());
  }
}

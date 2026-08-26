package ru.clicky.link.link;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity
public class Link {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  String shortLink;

  @Column(nullable = false)
  String originalUrl;

  Instant createdAt = Instant.now();

  private Long getId() {
    return id;
  }

  public void setShortLink(String shortLink) {
    this.shortLink = shortLink;
  }

  public String getShortLink() {
    return shortLink;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Link link)) {
      return false;
    }

    return getId().equals(link.getId());
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Override
  public String toString() {
    return "Link{shortLink=" + shortLink + ", originalUrl=" + originalUrl + "}";
  }
}

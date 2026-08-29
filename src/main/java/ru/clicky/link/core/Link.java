package ru.clicky.link.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.time.Instant;

@Entity
public class Link {
  @Id
  private Long id;

  @Column(nullable = false, unique = true, length = 12)
  private String shortUrl;

  @Column(nullable = false)
  private String originalUrl;

  private final Instant createdAt = Instant.now();

  public void setId(Long id) {
    this.id = id;
  }

  private Long getId() {
    return id;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public Instant getCreatedAt() {
    return createdAt;
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
    return "Link{shortUrl=" + shortUrl + ", originalUrl=" + originalUrl + "}";
  }
}

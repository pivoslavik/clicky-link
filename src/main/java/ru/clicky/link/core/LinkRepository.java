package ru.clicky.link.core;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LinkRepository {

  @PersistenceContext
  private EntityManager entityManager;

  private Session getSession() {
    return entityManager.unwrap(Session.class);
  }

  public Link save(Link link) {
    getSession().persist(link);
    return link;
  }

  public Long getNextId() {
    return ((Number) entityManager
        .createNativeQuery("SELECT nextval('links_seq')")
        .getSingleResult()).longValue();
  }

  public boolean existsByShortUrl(String shortUrl) {
    Long count = entityManager
        .createQuery("SELECT COUNT(l) FROM Link l WHERE l.shortUrl = :shortUrl", Long.class)
        .setParameter("shortUrl", shortUrl)
        .getSingleResult();
    return count > 0;
  }

  @Transactional(readOnly = true)
  public Optional<Link> findByShortUrl(String shortUrl) {
    return getSession()
        .createQuery("from Link l where l.shortUrl = :shortUrl", Link.class)
        .setParameter("shortUrl", shortUrl)
        .uniqueResultOptional();
  }
}

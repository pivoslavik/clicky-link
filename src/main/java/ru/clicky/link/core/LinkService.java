package ru.clicky.link.core;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.clicky.link.base62.Base62Converter;

@Service
public class LinkService {
  private static final long DEFAULT_SHIFT = 56_800_235_584L;

  private final LinkRepository linkRepository;
  private final Base62Converter base62Converter;

  public LinkService(LinkRepository linkRepository, Base62Converter base62Converter) {
    this.linkRepository = linkRepository;
    this.base62Converter = base62Converter;
  }

  @Transactional
  public Link createCustomLink(String originalUrl, String customAlias) {
    if (linkRepository.existsByShortUrl(customAlias)) {
      throw new LinkAlreadyExistsException("Short URL already in use: " + customAlias);
    }
    Long nextId = linkRepository.getNextId();
    return saveLink(nextId, customAlias, originalUrl);
  }

  @Transactional
  public Link createGeneratedLink(String originalUrl) {
    Long nextId;
    String shortUrl;
    do {
      nextId = linkRepository.getNextId();
      shortUrl = base62Converter.encode(nextId + DEFAULT_SHIFT);
    } while (linkRepository.existsByShortUrl(shortUrl));
    return saveLink(nextId, shortUrl, originalUrl);
  }

  private Link saveLink(Long nextId, String shortUrl, String originalUrl) {
    Link link = new Link();
    link.setId(nextId);
    link.setShortUrl(shortUrl);
    link.setOriginalUrl(originalUrl);
    return linkRepository.save(link);
  }
}

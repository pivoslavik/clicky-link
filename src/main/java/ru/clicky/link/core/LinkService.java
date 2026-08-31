package ru.clicky.link.core;

import org.springframework.stereotype.Service;
import ru.clicky.link.base62.Base62Converter;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LinkService {
  private static final long DEFAULT_SHIFT = 56_800_235_582L;

  private final LinkRepository linkRepository;
  private final Base62Converter base62Converter;

  public LinkService(LinkRepository linkRepository, Base62Converter base62Converter) {
    this.linkRepository = linkRepository;
    this.base62Converter = base62Converter;
  }

  @Transactional
  public LinkInfo createShortLink(LinkCreateRequest request) {
    Link link = !(request.alias().isBlank()) ? createCustomLink(request.url(), request.alias()) : createGeneratedLink(request.url());
    return LinkMapper.toInfo(link);
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

  @Transactional(readOnly = true)
  public Link redirect(String shortUrl) {
    return linkRepository.findByShortUrl(shortUrl).orElseThrow(() -> new LinkNotFoundException("Link Not Found with short url:" + shortUrl));
  }

  private Link saveLink(Long nextId, String shortUrl, String originalUrl) {
    Link link = new Link();
    link.setId(nextId);
    link.setShortUrl(shortUrl);
    link.setOriginalUrl(originalUrl);
    return linkRepository.save(link);
  }
}

package ru.clicky.link.core;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.clicky.link.common.constant.Routes;
import ru.clicky.link.ratelimiter.RateLimiter;

@RestController
public class LinkController {
  private final LinkService linkService;

  public LinkController(LinkService linkService) {
    this.linkService = linkService;
  }

  @PostMapping(Routes.LINK)
  @RateLimiter(key = "create_link_api", limit = 5, periodSeconds = 60)
  public ResponseEntity<LinkInfo> createShortLink(@Valid @RequestBody LinkCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(linkService.createShortLink(request));
  }

  @GetMapping(Routes.REDIRECT)
  @RateLimiter(key = "redirect_link_api", limit = 100, periodSeconds = 60)
  public ResponseEntity<Void> redirect(@PathVariable String slug) {
    Link link = linkService.redirect(slug);
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(link.getOriginalUrl())).build();
  }
}

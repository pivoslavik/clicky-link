package ru.clicky.link.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.clicky.link.common.constant.Routes;
import ru.clicky.link.core.LinkCreateRequest;
import ru.clicky.link.core.LinkService;
import ru.clicky.link.ratelimiter.RateLimiter;

@Controller
public class FormPage {
  private final LinkService linkService;

  @Value("${app.backend-url}")
  private String backendUrl;

  public FormPage(LinkService linkService) {
    this.linkService = linkService;
  }

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @PostMapping(Routes.PROCESS)
  @RateLimiter(key = "create_link_ui", limit = 10, periodSeconds = 60)
  public String processForm(@ModelAttribute LinkCreateRequest request, Model model) {
    model.addAttribute("linkInfo", linkService.createShortLink(request));
    model.addAttribute("baseUrl", backendUrl);
    return "index :: result-block";
  }
}

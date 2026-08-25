package ru.clicky.link.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clicky.link.common.response.MessageResponse;

@RestController
public class PingController {
  private static final String PING_MESSAGE = "OK";

  @GetMapping("/ping")
  MessageResponse ping(){
    return new MessageResponse(PING_MESSAGE);
  }
}

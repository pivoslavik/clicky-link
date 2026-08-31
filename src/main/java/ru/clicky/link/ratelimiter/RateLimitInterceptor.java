package ru.clicky.link.ratelimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
  private final StringRedisTemplate redisTemplate;

  public RateLimitInterceptor(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }
    RateLimiter rateLimit = handlerMethod.getMethodAnnotation(RateLimiter.class);
    if (rateLimit == null) {
      return true;
    }
    String clientIp = getClientIp(request);
    String redisKey = "rate_limit:" + rateLimit.key() + ":" + clientIp;
    long now = Instant.now().toEpochMilli();
    redisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, now - rateLimit.periodSeconds() * 1000L);
    Long currentCount = redisTemplate.opsForZSet().zCard(redisKey);
    if (currentCount != null && currentCount >= rateLimit.limit()) {
      throw new RateLimitExceededException("Request limit exceeded");
    }
    redisTemplate.opsForZSet().add(redisKey, UUID.randomUUID().toString(), now);
    redisTemplate.expire(redisKey, Duration.ofSeconds(rateLimit.periodSeconds() + 60));
    return true;
  }

  private String getClientIp(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader != null && !xfHeader.isEmpty()) {
      return xfHeader.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}

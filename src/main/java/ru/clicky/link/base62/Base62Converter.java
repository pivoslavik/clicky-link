package ru.clicky.link.base62;

import org.springframework.stereotype.Component;

@Component
public class Base62Converter {

  private static final int BASE = 62;
  private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  public String encode(long number) {
    StringBuilder stringBuilder = new StringBuilder(1);
    do {
      stringBuilder.insert(0, CHARACTERS.charAt((int) (number % BASE)));
      number /= BASE;
    } while (number > 0);
    return stringBuilder.toString();
  }

  public long decode(String number) {
    long result = 0L;
    int length = number.length();
    for (int i = 0; i < length; i++) {
      result += (long) Math.pow(BASE, i) * CHARACTERS.indexOf(number.charAt(length - i - 1));
    }
    return result;
  }
}

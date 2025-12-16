package com.apppalestrebookingbot.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class UtilityClass {

  private static final Dotenv dotenv = Dotenv.configure()
    .ignoreIfMissing()
    .load();

  public static String getEnv(String key) {
    String value = System.getenv(key);
    return value != null ? value : dotenv.get(key);
  }

}

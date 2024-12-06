package com.github.trganda.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionUtil {
  public static String getVersion() {
    Properties properties = new Properties();
    String version = "unknown";

    try (InputStream inputStream = VersionUtil.class.getResourceAsStream("version.properties")) {
      if (inputStream != null) {
        properties.load(inputStream);
        version = properties.getProperty("version", version);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return version;
  }
}

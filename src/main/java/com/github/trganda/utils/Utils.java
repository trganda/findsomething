package com.github.trganda.utils;

import com.github.trganda.FindSomething;
import com.github.trganda.utils.cache.CachePool;
import java.awt.Font;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

  public static String calHash(String... vals) {

    StringBuilder sb = new StringBuilder();
    for (String val : vals) {
      sb.append(val);
    }

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(sb.toString().getBytes());

      StringBuilder hexString = new StringBuilder();
      for (byte b : hashBytes) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      return "";
    }
  }

  public static Font getBurpDisplayFont() {
    return FindSomething.API.userInterface().currentDisplayFont();
  }

  public static Font getBurpEditorFont() {
    return FindSomething.API.userInterface().currentEditorFont();
  }

  public static List<String> aggregator(List<String> domains) {
    List<String> hosts = new ArrayList<>();
    Map<String, List<String>> aggregatedDomains =
        domains.stream().collect(Collectors.groupingBy(Utils::getRootDomain));

    aggregatedDomains.forEach(
        (rootDomain, domainList) -> {
          if (!rootDomain.startsWith("*")) {
            hosts.add("*." + rootDomain);
          }
        });

    return hosts;
  }

  // Method to extract the root domain from a full domain name
  public static String getRootDomain(String domain) {
    String[] parts = domain.split("\\.");
    int length = parts.length;
    // Return the last two parts as the root domain (e.g., "example.com")
    return length >= 2 ? parts[length - 2] + "." + parts[length - 1] : domain;
  }
}

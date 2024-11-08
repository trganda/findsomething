package com.github.trganda.utils;

import com.github.trganda.FindSomething;
import java.awt.Font;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

  public static String REQ = "REQ";

  public static String RESP = "RESP";

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
}

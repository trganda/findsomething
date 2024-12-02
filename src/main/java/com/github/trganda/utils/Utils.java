package com.github.trganda.utils;

import com.github.trganda.FindSomething;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

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

  public static boolean isDomainMatch(String pattern, String domain) {
    // If the pattern and domain are exactly the same, they match
    if (pattern.equals(domain) || pattern.equals("*")) {
      return true;
    }

    // Check if the pattern starts with a wildcard (*.)
    if (pattern.startsWith("*.")) {
      // Remove the "*." from the pattern to get the base domain (example.com)
      String baseDomain = pattern.substring(2);

      // Check if the domain ends with the base domain and has a dot before it
      // This ensures sub.example.com matches *.example.com but example.com does not match
      // *.example.com
      return domain.endsWith("." + baseDomain);
    }

    // If no conditions matched, return false
    return false;
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

  public static BufferedImage convertSVGToPNG(String url) throws TranscoderException, IOException {
    ByteArrayOutputStream resultByteStream = new ByteArrayOutputStream();

    TranscoderInput transcoderInput = new TranscoderInput(url);
    TranscoderOutput transcoderOutput = new TranscoderOutput(resultByteStream);

    PNGTranscoder pngTranscoder = new PNGTranscoder();
    pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, 512f);
    pngTranscoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, 512f);
    pngTranscoder.transcode(transcoderInput, transcoderOutput);

    resultByteStream.flush();

    BufferedImage largeImage =
        ImageIO.read(new ByteArrayInputStream(resultByteStream.toByteArray()));

    int fontSize = UIManager.getFont("Button.font").getSize();
    BufferedImage smallImage = new BufferedImage(fontSize, fontSize, BufferedImage.TYPE_INT_ARGB);
    Graphics2D gSmall = smallImage.createGraphics();
    gSmall.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    gSmall.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    gSmall.drawImage(largeImage, 0, 0, 16, 16, null);
    gSmall.dispose();

    return largeImage;
  }
}

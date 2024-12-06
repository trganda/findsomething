package com.github.trganda.handler;

import com.github.trganda.config.ConfigManager;
import com.github.trganda.config.Rules;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MatcherTest {

  private static Rules rules;

  @BeforeAll
  public static void loadTestRules() {
    rules = ConfigManager.getInstance().loadRules(true);
  }

  @Test
  public void testURLRule() {
    InputStream is = MatcherTest.class.getClassLoader().getResourceAsStream("app.dae7b379.js");
    try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
      StringBuilder sb = new StringBuilder();
      int ch;
      while ((ch = reader.read()) != -1) {
        sb.append((char) ch);
      }
      // System.out.println(sb.toString());
      rules.getGroups().stream()
          .filter(g -> g.getGroup().equals("Information"))
          .findFirst()
          .ifPresent(
              g -> {
                g.getRule().stream()
                    .filter(r -> r.getName().equals("Linkfinder"))
                    .findFirst()
                    .ifPresent(
                        r -> {
                          System.out.println(r.getName());
                          Pattern pattern =
                              Pattern.compile(
                                  r.getRegex(), r.isSensitive() ? 0 : Pattern.CASE_INSENSITIVE);
                          String[] results = match(sb.toString(), pattern, new int[] {1});
                          for (String result : results) {
                            System.out.println(result);
                          }
                          System.out.println(results.length);
                        });
              });

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGroupMatch() {
    String regex =
        "\\W((?:(?:\\+|00)86)?(1(?:3[\\d]|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8[\\d]|9[189]))\\d{8})\\W";
    String text = " 19197284453 ";

    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    String[] results = match(text, pattern, new int[] {1});
    Arrays.stream(results).forEach(System.out::println);
  }

  private String[] match(String text, Pattern pattern) {
    Matcher matcher = pattern.matcher(text);
    HashSet<String> set = new HashSet<>();

    while (matcher.find()) {
      set.add(matcher.group());
    }

    return set.toArray(new String[0]);
  }

  private String[] match(String text, Pattern pattern, int[] groups) {
    Matcher matcher = pattern.matcher(text);
    HashSet<String> set = new HashSet<>();

    while (matcher.find()) {
      Arrays.stream(groups)
          .mapToObj(
              g -> {
                try {
                  return matcher.group(g);
                } catch (IndexOutOfBoundsException e) {
                  return "";
                }
              })
          .filter(s -> !s.isEmpty())
          .forEach(set::add);
    }

    return set.toArray(new String[0]);
  }
}

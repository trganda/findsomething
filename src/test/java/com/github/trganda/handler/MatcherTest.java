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
    // https://g.alicdn.com/aliretail/microfront-app/1.0.32/static/js/main.js
    InputStream is = MatcherTest.class.getClassLoader().getResourceAsStream("main.js");
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
                    .filter(r -> r.getName().equals("Link"))
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

  @Test
  public void testRule() {
    String regex = rules.getRulesWithGroup("Vulnerability").stream().filter(r -> r.getName().equals("IDOR Parameters")).findFirst().get().getRegex();
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    String text = "/ztbox?action=zpblog&appname=pcsearch&sid=61027_61219_60853_61505_61525_61680_61735_61780_61794&v=2.0&data=%7B%22cateid%22%3A%2299%22%2C%22actiondata%22%3A%7B%22id%22%3A18463%2C%22type%22%3A%220%22%2C%22timestamp%22%3A1736503683875%2C%22content%22%3A%7B%22page%22%3A%22home%22%2C%22source%22%3A%22%22%2C%22from%22%3A%22search%22%2C%22type%22%3A%22display%22%2C%22value%22%3A%22%22%2C%22ext%22%3A%7B%22status%22%3A%22default%22%2C%22is_log%22%3A%220%22%2C%22have_hotsearch%22%3A%221%22%7D%7D%7D%7D";
    String[] results = match(text, pattern, new int[] {0});

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

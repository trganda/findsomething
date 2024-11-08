package com.github.trganda.handler;

import com.github.trganda.config.Config;
import com.github.trganda.config.Rules;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class MatcherTest {

  @Test
  public void test() {
    Rules rules = Config.loadRules();

    InputStream is = MatcherTest.class.getClassLoader().getResourceAsStream("nav.js");
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
                    .filter(r -> r.getName().equals("All URL"))
                    .findFirst()
                    .ifPresent(
                        r -> {
                          System.out.println(r.getName());
                          Pattern pattern =
                              Pattern.compile(
                                  r.getRegex(), r.isSensitive() ? 0 : Pattern.CASE_INSENSITIVE);
                          String[] results = match(sb.toString(), pattern);
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

  private String[] match(String text, Pattern pattern) {
    Matcher matcher = pattern.matcher(text);
    HashSet<String> set = new HashSet<>();

    while (matcher.find()) {
      set.add(matcher.group());
    }

    return set.toArray(new String[0]);
  }
}
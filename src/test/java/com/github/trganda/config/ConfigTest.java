package com.github.trganda.config;

import static com.github.trganda.config.Config.getYaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class ConfigTest {

  @Test
  public void testLoadRules() {
    // using the default configuration if no local configuration file.
    InputStream is = Config.class.getClassLoader().getResourceAsStream("rules.yml");
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    Rules rules = getYaml().loadAs(reader, Rules.class);
    if (rules == null) {
      System.out.println("rules is null");
    }
  }

  @Test
  public void testLoadConfig() {
    InputStream is = Config.class.getClassLoader().getResourceAsStream("config.yml");
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    Config config = getYaml().loadAs(reader, Config.class);
    if (config == null) {
      System.out.println("config is null");
    }
  }

  @Test
  public void testSaveConfig() {
    Config config = Config.getInstance();
    System.out.println("config: " + Config.getYaml().dump(config));
  }
}

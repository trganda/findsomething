package com.github.trganda.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class ConfigManagerTest {

  @Test
  public void testLoadRules() {
    InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("rules.yml");
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    Rules rules = YamlLoader.getYaml().loadAs(reader, Rules.class);
    if (rules == null) {
      System.out.println("rules is null");
    }
  }

  @Test
  public void testLoadConfig() {
    InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("config.yml");
    Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    Config config = YamlLoader.getYaml().loadAs(reader, Config.class);
    if (config == null) {
      System.out.println("config is null");
    }
  }

  @Test
  public void testSaveConfig() {
    ConfigManager configManager = ConfigManager.getInstance();
    System.out.println("config: " + YamlLoader.getYaml().dump(configManager));
  }
}

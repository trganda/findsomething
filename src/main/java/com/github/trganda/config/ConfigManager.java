package com.github.trganda.config;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Rules.Rule;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import lombok.Data;

@Data
public class ConfigManager implements ConfigChangeListener {

  public static final String GROUP_GENERAL = "General";

  public static final String GROUP_FINGERPRINT = "Fingerprint";

  public static final String GROUP_SENSITIVE = "Sensitive";

  public static final String GROUP_INFORMATION = "Information";

  public static final String GROUP_VULNERABILITY = "Vulnerability";

  private static final String configLocation =
      String.format("%s/.config/fd/fd_config.yaml", System.getProperty("user.home"));

  private static final String rulesLocation =
      String.format("%s/.config/fd/fd_rules.yaml", System.getProperty("user.home"));

  private Rules rules;
  private Config config;
  private List<ConfigChangeListener> listeners = new ArrayList<>();

  private static ConfigManager configManager;

  private ConfigManager() {
    config = loadConfig();
    rules = loadRules(false);
  }

  public static ConfigManager getInstance() {
    if (configManager == null) {
      configManager = new ConfigManager();
    }
    return configManager;
  }

  public void syncSuffixes(String suffix, Operation type) {
    switch (type) {
      case ADD:
        config.getSuffixes().add(suffix);
        break;
      case DEL:
        config.getSuffixes().remove(suffix);
        break;
      case CLR:
        config.getSuffixes().clear();
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void syncHosts(String suffix, Operation type) {
    switch (type) {
      case ADD:
        config.getHosts().add(suffix);
        break;
      case DEL:
        config.getHosts().remove(suffix);
        break;
      case CLR:
        config.getHosts().clear();
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void syncStatus(String suffix, Operation type) {
    switch (type) {
      case ADD:
        config.getStatus().add(suffix);
        break;
      case DEL:
        config.getStatus().remove(suffix);
        break;
      case CLR:
        config.getStatus().clear();
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void syncRules(String group, Rule rule, Operation type) {
    switch (type) {
      case ADD:
        configManager.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(
                g -> {
                  g.getRule().add(rule);
                });
        break;
      case ENB:
        configManager.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(
                g -> {
                  g.getRule().stream()
                      .filter(r -> r.getName().equals(rule.getName()))
                      .findFirst()
                      .ifPresent(
                          r -> {
                            r.setEnabled(rule.isEnabled());
                          });
                });
        break;
      case DEL:
        configManager.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(g -> g.getRule().remove(rule));
        break;
      case CLR:
        configManager.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(g -> g.getRule().clear());
        break;
    }
    notifyListeners();
  }

  public Config loadConfig() {
    Config config = null;
    try (InputStreamReader reader =
        new InputStreamReader(
            Files.newInputStream(Paths.get(configLocation)), StandardCharsets.UTF_8)) {
      config = YamlLoader.getYaml().loadAs(reader, Config.class);
    } catch (IOException e) {
      InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("config.yml");
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      config = YamlLoader.getYaml().loadAs(reader, Config.class);
    }

    return config;
  }

  public Rules loadRules(boolean defaultRules) {
    Rules rules = null;
    if (defaultRules) {
      InputStream is = ConfigManager.class.getClassLoader().getResourceAsStream("rules.yml");
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      rules = YamlLoader.getYaml().loadAs(reader, Rules.class);
    } else {
      try (InputStreamReader reader =
          new InputStreamReader(
              Files.newInputStream(Paths.get(rulesLocation)), StandardCharsets.UTF_8)) {
        rules = YamlLoader.getYaml().loadAs(reader, Rules.class);
      } catch (IOException e) {
        rules = loadRules(true);
      }
    }

    return rules;
  }

  public void saveConfig() {
    if (config == null) {
      return;
    }

    saveConfig(config);
  }

  private void saveConfig(Config config) {
    // create config directory if not exists
    Paths.get(configLocation).toFile().getParentFile().mkdirs();
    try {
      Writer ws =
          new OutputStreamWriter(
              Files.newOutputStream(Paths.get(configLocation)), StandardCharsets.UTF_8);
      YamlLoader.getYaml().dump(config, ws);
      ws.close();
    } catch (Exception e) {
      FindSomething.API.logging().logToError("Saving configuration file failed, ", e);
    }
  }

  public void saveRules() {
    if (configManager == null) {
      return;
    }

    saveRules(configManager.rules);
  }

  private void saveRules(Rules rules) {
    // create config directory if not exists
    Paths.get(configLocation).toFile().getParentFile().mkdirs();
    try {
      Writer ws =
          new OutputStreamWriter(
              Files.newOutputStream(Paths.get(rulesLocation)), StandardCharsets.UTF_8);
      YamlLoader.getYaml().dump(rules, ws);
      ws.close();
    } catch (Exception e) {
      FindSomething.API.logging().logToError("Saving rules file failed, ", e);
    }
  }

  /**
   * Notifies all registered config listeners that the config has changed.
   *
   * <p>This is called after the config has been saved.
   */
  private void notifyListeners() {
    for (ConfigChangeListener listener : listeners) {
      listener.onConfigChange(this);
    }
  }

  public void registerConfigListener(ConfigChangeListener listener) {
    listeners.add(listener);
  }

  @Override
  public void onConfigChange(ConfigManager configManager) {
    saveConfig(configManager.getConfig());
    saveRules(configManager.getRules());
  }
}

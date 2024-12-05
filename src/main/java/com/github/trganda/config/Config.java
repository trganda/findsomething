package com.github.trganda.config;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.utils.Utils;
import com.github.trganda.utils.cache.CachePool;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import lombok.Data;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

@Data
public class Config implements ConfigChangeListener {

  public static final String BLACKLIST_SUFFIX = "Suffix";

  public static final String BLACKLIST_HOST = "Host";

  public static final String BLACKLIST_STATUS = "Status";

  public static final String GROUP_GENERAL = "General";

  public static final String GROUP_FINGERPRINT = "Fingerprint";

  public static final String GROUP_SENSITIVE = "Sensitive";

  public static final String GROUP_INFORMATION = "Information";

  public static final String GROUP_VULNERABILITY = "Vulnerability";

  private static final String configLocation =
      String.format("%s/.config/fd/fd_config.json", System.getProperty("user.home"));

  private static final String rulesLocation =
      String.format("%s/.config/fd/fd_rules.json", System.getProperty("user.home"));

  private List<String> suffixes = new ArrayList<>();
  private List<String> hosts = new ArrayList<>();
  private List<String> status = new ArrayList<>();
  private transient Rules rules;

  private List<ConfigChangeListener> listeners = new ArrayList<>();

  private static Config config;

  public static Config getInstance() {
    if (config == null) {
      FindSomething.API.logging().logToOutput("loading configuration file...");
      config = loadConfig();
      config.rules = loadRules(false);
    }
    return config;
  }

  public void syncSuffixes(String suffix, Operation type) {
    switch (type) {
      case ADD:
        config.suffixes.add(suffix);
        break;
      case DEL:
        config.suffixes.remove(suffix);
        break;
      case CLR:
        config.suffixes.clear();
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void syncHosts(String suffix, Operation type) {
    switch (type) {
      case ADD:
        config.hosts.add(suffix);
        break;
      case DEL:
        config.hosts.remove(suffix);
        break;
      case CLR:
        config.hosts.clear();
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void syncStatus(String suffix, Operation type) {
    switch (type) {
      case ADD:
        config.status.add(suffix);
        break;
      case DEL:
        config.status.remove(suffix);
        break;
      case CLR:
        config.status.clear();
        break;
      default:
        break;
    }
    notifyListeners();
  }

  public void syncRules(String group, Rule rule, Operation type) {
    switch (type) {
      case ADD:
        config.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(
                g -> {
                  g.getRule().add(rule);
                });
        break;
      case ENB:
        config.rules.getGroups().stream()
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
        config.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(g -> g.getRule().remove(rule));
        break;
      case CLR:
        config.rules.getGroups().stream()
            .filter(g -> g.getGroup().equals(group))
            .findFirst()
            .ifPresent(g -> g.getRule().clear());
        break;
    }
    notifyListeners();
  }

  public static Yaml getYaml() {
    DumperOptions dop = new DumperOptions();
    dop.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    dop.setExplicitStart(false);
    Representer representer = new Representer(dop);
    representer.addClassTag(Config.class, Tag.MAP);
    representer.addClassTag(Rules.class, Tag.MAP);
    return new Yaml(representer, dop);
  }

  public static Config loadConfig() {
    Config config = null;
    try (InputStreamReader reader =
        new InputStreamReader(
            Files.newInputStream(Paths.get(configLocation)), StandardCharsets.UTF_8)) {
      config = getYaml().loadAs(reader, Config.class);
    } catch (IOException e) {
      FindSomething.API
          .logging()
          .logToError("load configuration file failed, using default config");

      InputStream is = Config.class.getClassLoader().getResourceAsStream("config.yml");
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      config = getYaml().loadAs(reader, Config.class);
    }

    return config;
  }

  public static Rules loadRules(boolean defaultRules) {
    Rules rules = null;
    if (defaultRules) {
      InputStream is = Config.class.getClassLoader().getResourceAsStream("rules.yml");
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      rules = getYaml().loadAs(reader, Rules.class);
    } else {
      try (InputStreamReader reader =
          new InputStreamReader(
              Files.newInputStream(Paths.get(rulesLocation)), StandardCharsets.UTF_8)) {
        rules = getYaml().loadAs(reader, Rules.class);
      } catch (IOException e) {
        FindSomething.API.logging().logToError("load rules file failed, using default config");
        rules = loadRules(true);
      }
    }

    // saving rules to cache for future use, the key was a hash that generate from group name and
    // rule name.
    rules
        .getGroups()
        .forEach(
            g -> {
              g.getRule()
                  .forEach(
                      r -> {
                        String key = Utils.calHash(g.getGroup(), r.getName());
                        CachePool.getInstance().putRule(key, r);
                      });
            });

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
      getYaml().dump(config, ws);
      ws.close();
      // FindSomething.API.logging().logToOutput("Saved configuration to " + configLocation);
    } catch (Exception e) {
      FindSomething.API.logging().logToError("Saving configuration file failed, ", e);
    }
  }

  public void saveRules() {
    if (config == null) {
      return;
    }

    saveRules(config.rules);
  }

  private void saveRules(Rules rules) {
    // create config directory if not exists
    Paths.get(configLocation).toFile().getParentFile().mkdirs();
    try {
      Writer ws =
          new OutputStreamWriter(
              Files.newOutputStream(Paths.get(rulesLocation)), StandardCharsets.UTF_8);
      getYaml().dump(rules, ws);
      ws.close();
      // FindSomething.API.logging().logToOutput("Saved rules to " + rulesLocation);
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
    for (ConfigChangeListener listener : config.listeners) {
      listener.onConfigChange(config);
    }
  }

  public void registerConfigListener(ConfigChangeListener listener) {
    config.listeners.add(listener);
  }

  @Override
  public void onConfigChange(Config config) {
    saveConfig(config);
    saveRules(config.rules);
  }
}

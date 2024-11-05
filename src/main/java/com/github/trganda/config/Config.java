package com.github.trganda.config;

import com.github.trganda.FindSomething;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.model.cache.CachePool;
import com.github.trganda.utils.Utils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class Config implements ConfigChangeListener {

  public static final String BLACKLIST_SUFFIX = "Suffix";

  public static final String BLACKLIST_HOST = "Host";

  public static final String BLACKLIST_STATUS = "Status";

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
  private Rules rules;

  private List<ConfigChangeListener> listeners = new ArrayList<>();

  private static Config config;

  public static Config getInstance() {
    if (config == null) {
      FindSomething.API.logging().logToOutput("loading configuration file...");
      config = loadConfig();
    }
    return config;
  }

  public void addSuffix(String suffix) {
    config.suffixes.add(suffix);
    notifyListeners();
  }

  public void addHost(String host) {
    config.hosts.add(host);
    notifyListeners();
  }

  public void addStatus(String statusCode) {
    config.status.add(statusCode);
    notifyListeners();
  }

  public void addRule(String group, Rule rule) {
    config.getRules().getRules().stream()
        .filter(g -> g.getGroup().equals(group))
        .findFirst()
        .ifPresent(g -> g.getRule().add(rule));
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

  public List<String> getSuffixes() {
    return suffixes;
  }

  public void setSuffixes(List<String> suffixes) {
    this.suffixes = suffixes;
  }

  public List<String> getHosts() {
    return hosts;
  }

  public void setHosts(List<String> hosts) {
    this.hosts = hosts;
  }

  public List<String> getStatus() {
    return status;
  }

  public void setStatus(List<String> status) {
    this.status = status;
  }

  public Rules getRules() {
    return rules;
  }

  public void setRules(Rules rules) {
    this.rules = rules;
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

  public static void loadRules() {
    File configFile = new File(rulesLocation);
    InputStream is;
    try {
      if (!configFile.exists()) {
        // using the default configuration if no local configuration file.
        is = Config.class.getClassLoader().getResourceAsStream("rules.yml");
      } else {
        is = Files.newInputStream(Paths.get(rulesLocation));
      }
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      Rules rules = getYaml().loadAs(reader, Rules.class);
      Config.getInstance().setRules(rules);
      // loading rules to cache
      rules
          .getRules()
          .forEach(
              g -> {
                g.getRule()
                    .forEach(
                        r -> {
                          String key = Utils.calHash(g.getGroup(), r.getName());
                          CachePool.putRule(key, r);
                        });
              });
    } catch (IOException e) {
      FindSomething.API.logging().logToError(e);
    }
  }

  public static void saveConfig() {
    if (config == null) {
      return;
    }

    saveConfig(config);
  }

  private static void saveConfig(Config config) {
    try {
      Writer ws =
          new OutputStreamWriter(
              Files.newOutputStream(Paths.get(configLocation)), StandardCharsets.UTF_8);
      getYaml().dump(config, ws);
      ws.close();
      FindSomething.API.logging().logToOutput("Saved config to " + configLocation);
    } catch (Exception e) {
      FindSomething.API.logging().logToError("Save configuration file failed, ", e);
    }
  }

  public static void saveRules() {
    try {
      Writer ws =
          new OutputStreamWriter(
              Files.newOutputStream(Paths.get(rulesLocation)), StandardCharsets.UTF_8);
      getYaml().dump(config.rules, ws);
      ws.close();
      FindSomething.API.logging().logToOutput("Saved rules to " + rulesLocation);
    } catch (Exception e) {
      FindSomething.API.logging().logToError("Save configuration file failed, ", e);
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
  }
}

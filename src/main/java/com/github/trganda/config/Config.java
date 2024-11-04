package com.github.trganda.config;

import com.github.trganda.FindSomething;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Config {

    public static final String BLACKLIST_SUFFIX = "Suffix";

    public static final String BLACKLIST_HOST = "Host";

    public static final String BLACKLIST_STATUS = "Status";

    public static final String GROUP_FINGERPRINT = "Fingerprint";

    public static final String GROUP_SENSITIVE = "Sensitive";

    public static final String GROUP_INFORMATION = "Information";

    public static final String GROUP_VULNERABILITY = "Vulnerability";

    private static final String configLocation = String.format("%s/.config/fd_config.json", System.getProperty("user.home"));

    private static final String rulesLocation = String.format("%s/.config/fd_rules.json", System.getProperty("user.home"));

    private List<String> suffixes = new ArrayList<>();
    private List<String> hosts = new ArrayList<>();
    private List<String> status = new ArrayList<>();

    private Rules rules;
    private static Config config;

    public static Config getInstance() {
        if (config == null) {
            loadConfig();
            loadRules();
        }
        return config;
    }

    private static Yaml getYaml() {
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

    public static void addSuffix(String suffix) {
        Config.getInstance().suffixes.add(suffix);
        saveConfig();
    }

    public static void addHost(String host) {
        Config.getInstance().hosts.add(host);
        saveConfig();
    }

    public static void addStatus(String statusCode) {
        Config.getInstance().status.add(statusCode);
        saveConfig();
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> castToList(Object obj) {
        return (List<T>) obj;
    }

    public static void loadConfig() {
        File configFile = new File(configLocation);
        InputStream is;
        try {
            if (!configFile.exists()) {
                // using the default configuration if no local configuration file.
                is = Config.class.getClassLoader().getResourceAsStream("config.yml");
            } else {
                is = Files.newInputStream(Paths.get(configLocation));
            }
            if (is != null) {
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                config = getYaml().loadAs(reader, Config.class);
            }
        } catch (Exception e) {
//            FindSomething.API.logging().logToError(e);
        }
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
            if (is != null) {
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                Rules rules = getYaml().loadAs(reader, Rules.class);
                Config.getInstance().setRules(rules);
            }
        } catch (Exception e) {
            FindSomething.API.logging().logToError(e);
        }
    }

    public static void saveConfig() {
        if (config == null) {
            return;
        }

        try {
            Writer ws = new OutputStreamWriter(Files.newOutputStream(Paths.get(configLocation)), StandardCharsets.UTF_8);
            getYaml().dump(config, ws);
            ws.close();
//            FindSomething.API.logging().logToOutput("Saved config to " + configLocation);
        } catch (Exception e) {
//            FindSomething.API.logging().logToError(e);
        }
    }

    public static void saveRules() {
        try {
            Writer ws = new OutputStreamWriter(Files.newOutputStream(Paths.get(rulesLocation)), StandardCharsets.UTF_8);
            getYaml().dump(config.rules, ws);
            ws.close();
            FindSomething.API.logging().logToOutput("Saved rules to " + rulesLocation);
        } catch (Exception e) {
            FindSomething.API.logging().logToError(e);
        }
    }
}

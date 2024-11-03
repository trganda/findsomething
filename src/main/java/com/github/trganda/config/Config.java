package com.github.trganda.config;

import com.github.trganda.FindSomething;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
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

    public static final String GROUP_ADD = "Add type";

    public static final String GROUP_FINGERPRINT = "Fingerprint";

    public static final String GROUP_SENSITIVE = "Sensitive";

    public static final String GROUP_INFORMATION = "Information";

    public static final String GROUP_VULNERABILITY = "Vulnerability";

    public static final String configLocation = String.format("%s/.config/fd_config.json", System.getProperty("user.home"));
//    public static String suffixes = "g2|3gp|7z|aac|abw|aif|aifc|aiff|apk|arc|au|avi|azw|bat|bin|bmp|bz|bz2|cmd|cmx|cod|com|csh|css|csv|dll|doc|docx|ear|eot|epub|exe|flac|flv|gif|gz|ico|ics|ief|jar|jfif|jpe|jpeg|jpg|less|m3u|mid|midi|mjs|mkv|mov|mp2|mp3|mp4|mpa|mpe|mpeg|mpg|mpkg|mpp|mpv2|odp|ods|odt|oga|ogg|ogv|ogx|otf|pbm|pdf|pgm|png|pnm|ppm|ppt|pptx|ra|ram|rar|ras|rgb|rmi|rtf|scss|sh|snd|svg|swf|tar|tif|tiff|ttf|vsd|war|wav|weba|webm|webp|wmv|woff|woff2|xbm|xls|xlsx|xpm|xul|xwd|zip";
    public static List<String> suffixes = new ArrayList<>();
    public static List<String> hosts = new ArrayList<>();

    public static List<String> status = new ArrayList<>();

    private static Yaml getYaml() {
        DumperOptions dop = new DumperOptions();
        dop.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer(dop);
        return new Yaml(representer, dop);
    }

    public static void addSuffix(String suffix) {
        suffixes.add(suffix);
        saveConfig();
    }

    public static String[] getSuffixes() {
        return suffixes.toArray(new String[0]);
    }

    public static void addHost(String host) {
        hosts.add(host);
        saveConfig();
    }

    public static String[] getHosts() {
        return hosts.toArray(new String[0]);
    }

    public static void addStatus(String statusCode) {
        status.add(statusCode);
        saveConfig();
    }

    public static String[] getStatus() {
        return status.toArray(new String[0]);
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
                FindSomething.API.logging().logToOutput("Loading config.yml from inner file.");
            } else {
                is = Files.newInputStream(Paths.get(configLocation));
            }
            if (is != null) {
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                Map<String, Object> config = getYaml().load(reader);
                suffixes = castToList(config.get("suffixes"));
                hosts = castToList(config.get("hosts"));
                status = castToList(config.get("status"));
            }
        } catch (Exception e) {
            FindSomething.API.logging().logToError(e);
        }
    }

    public static void saveConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("suffixes", suffixes.toArray(new String[0]));
        config.put("hosts", hosts.toArray(new String[0]));
        config.put("status", status.toArray(new String[0]));
        try {
            Writer ws = new OutputStreamWriter(Files.newOutputStream(Paths.get(configLocation)), StandardCharsets.UTF_8);
            getYaml().dump(config, ws);
            ws.close();
            FindSomething.API.logging().logToOutput("Saved config to " + configLocation);
        } catch (Exception e) {
            FindSomething.API.logging().logToError(e);
        }
    }
}

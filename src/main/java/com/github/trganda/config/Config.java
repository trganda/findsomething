package com.github.trganda.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final String BLACKLIST_SUFFIX = "Suffix";

    public static final String BLACKLIST_HOST = "Host";

    public static final String BLACKLIST_STATUS = "Status";

    public static final String GROUP_FINGERPRINT = "Fingerprint";

    public static final String GROUP_SENSITIVE = "Sensitive";

    public static final String GROUP_INFORMATION = "Information";

    public static final String GROUP_VULNERABILITY = "Vulnerability";

    public static String suffixes = "g2|3gp|7z|aac|abw|aif|aifc|aiff|apk|arc|au|avi|azw|bat|bin|bmp|bz|bz2|cmd|cmx|cod|com|csh|css|csv|dll|doc|docx|ear|eot|epub|exe|flac|flv|gif|gz|ico|ics|ief|jar|jfif|jpe|jpeg|jpg|less|m3u|mid|midi|mjs|mkv|mov|mp2|mp3|mp4|mpa|mpe|mpeg|mpg|mpkg|mpp|mpv2|odp|ods|odt|oga|ogg|ogv|ogx|otf|pbm|pdf|pgm|png|pnm|ppm|ppt|pptx|ra|ram|rar|ras|rgb|rmi|rtf|scss|sh|snd|svg|swf|tar|tif|tiff|ttf|vsd|war|wav|weba|webm|webp|wmv|woff|woff2|xbm|xls|xlsx|xpm|xul|xwd|zip";

    public static String[] hosts = new String[]{"trganda.top"};

    public static final String configLocation = String.format("%s/.config/fd_config.json", System.getProperty("user.home"));

    private static Yaml getYaml() {
        DumperOptions dop = new DumperOptions();
        dop.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer(dop);
        return new Yaml(representer, dop);
    }

    public static String[] getSuffixes() {
        return suffixes.split("\\|");
    }

    public static String[] getHosts() {
        return hosts.clone();
    }

    public static void loadConfig() {
        File configFile = new File(configLocation);
        InputStream is;
        try {
            if (!configFile.exists()) {
                is = Config.class.getResourceAsStream("config.yml");
            } else {
                is = Files.newInputStream(Paths.get(configLocation));
            }
            if (is != null) {
                Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                Map<String, Object> config = getYaml().load(reader);
                suffixes = config.get("suffixes").toString();
                hosts = (String[]) config.get("hosts");
            }
        } catch (Exception ignored) {
        }
    }

    public static void saveConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("suffixes", suffixes);
        config.put("hosts", hosts);
        try {
            Writer ws = new OutputStreamWriter(Files.newOutputStream(Paths.get(configLocation)), StandardCharsets.UTF_8);
            getYaml().dump(config, ws);
            ws.close();
        } catch (IOException ignored) {
        }
    }
}

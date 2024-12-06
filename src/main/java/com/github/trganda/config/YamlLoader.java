package com.github.trganda.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class YamlLoader {

  public static Yaml getYaml() {
    DumperOptions dop = new DumperOptions();
    dop.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    dop.setExplicitStart(false);
    Representer representer = new Representer(dop);

    //    TypeDescription typeDescription = new TypeDescription(ConfigManager.class);
    //    // fields to exclude
    //    typeDescription.setExcludes("rules", "listeners");
    //    representer.addTypeDescription(typeDescription);

    // ignore class name
    representer.addClassTag(Config.class, Tag.MAP);
    representer.addClassTag(Rules.class, Tag.MAP);

    return new Yaml(representer, dop);
  }
}

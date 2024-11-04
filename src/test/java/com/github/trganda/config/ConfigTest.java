package com.github.trganda.config;

import org.junit.jupiter.api.Test;

public class ConfigTest {

//    @Test
//    public void testLoadRules() {
//        Config.loadRules();
//    }
//
//    @Test
//    public void testLoadConfig() {
//        Config.loadConfig();
//        System.out.println(Config.getInstance().getSuffixes());
//    }

    @Test
    public void testSaveConfig() {
        Config.loadConfig();
        Config.saveConfig();
    }
}

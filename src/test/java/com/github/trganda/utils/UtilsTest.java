package com.github.trganda.utils;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  public void testAggregator() {
    String[] domains =
        new String[] {
          "*",
          "*.baidu.com",
          "*.bdstatic.com",
          "www.baidu.com",
          "passport.baidu.com",
          "bce.bdstatic.com",
          "hm.baidu.com",
          "fxgate.baidu.com",
          "bce.baidu.com",
          "console.bce.baidu.com",
          "miao.baidu.com",
          "cloud.baidu.com"
        };

    List<String> hosts = Utils.aggregator(Arrays.asList(domains));

    for (String host : hosts) {
      System.out.println(host);
    }
  }

  @Test
  public void testGetRootDomain() {
    String[] domains =
        new String[] {
          "www.baidu.com",
          "passport.baidu.com",
          "bce.bdstatic.com",
          "hm.baidu.com",
          "fxgate.baidu.com",
          "bce.baidu.com",
          "console.bce.baidu.com",
          "miao.baidu.com",
          "cloud.baidu.com"
        };

    for (String domain : domains) {
      System.out.println(Utils.getRootDomain(domain));
    }
  }

  @Test
  public void testCallHash() {
    String ret = Utils.calHash("1");
    System.out.println(ret);
  }
}

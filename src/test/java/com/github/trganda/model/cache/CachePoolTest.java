package com.github.trganda.model.cache;

import com.github.trganda.model.InfoDataModel;
import com.github.trganda.utils.cache.CachePool;
import org.junit.jupiter.api.Test;

public class CachePoolTest {

  @Test
  public void testGetInfoData() {

    CachePool cachePool = CachePool.getInstance();
    cachePool.addInfoDataModel("test", new InfoDataModel("rule", "test", "example.com"));
    System.out.println(cachePool.getInfoData("test"));
  }
}

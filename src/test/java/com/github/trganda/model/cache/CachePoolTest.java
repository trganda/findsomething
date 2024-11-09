package com.github.trganda.model.cache;

import com.github.trganda.model.InfoDataModel;
import org.junit.jupiter.api.Test;

public class CachePoolTest {

  @Test
  public void testGetInfoData() {

    CachePool cachePool = CachePool.getInstance();
    cachePool.addInfoDataModel("test", new InfoDataModel("test"));
    System.out.println(cachePool.getInfoData("test"));
  }
}

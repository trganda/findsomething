package com.github.trganda.model.cache;

import org.junit.jupiter.api.Test;

import com.github.trganda.model.InfoDataModel;

public class CachePoolTest {

    @Test
    public void testGetInfoData() {

        CachePool cachePool = CachePool.getInstance();
        cachePool.addInfoDataModel("test", new InfoDataModel("test"));
        System.out.println(cachePool.getInfoData("test"));
    }
}

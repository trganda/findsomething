package com.github.trganda.model.cache;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachePool {

  private static final int MAX_SIZE = 100000;
  private static final int EXPIRES_IN_HOURS = 3;
  private static final Cache<String, InterceptedResponse> httpMessageCache =
      Caffeine.newBuilder()
          .maximumSize(MAX_SIZE)
          .expireAfterWrite(EXPIRES_IN_HOURS, TimeUnit.HOURS)
          .build();
  private static final Cache<String, List<InfoDataModel>> infoCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();
  private static final Cache<String, List<RequestDetailModel>> reqInfoCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();
  private static final Cache<String, Rule> ruleCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();

  private static CachePool instance;

  public static CachePool getInstance() {
    if (instance == null) {
      instance = new CachePool();
    }
    return instance;
  }

  public void putRequestDataModel(String key, List<RequestDetailModel> requestDataModels) {
    reqInfoCache.put(key, requestDataModels);
  }

  public void addRequestDataModel(String key, RequestDetailModel requestDataModel) {
    List<RequestDetailModel> vals = reqInfoCache.getIfPresent(key);
    if (vals == null || vals.isEmpty()) {
      putRequestDataModel(key, List.of(requestDataModel));
    } else {
      // must copy the list to avoid the thread crash
      List<RequestDetailModel> copyVals = new ArrayList<>(vals);
      copyVals.add(requestDataModel);
      reqInfoCache.put(key, copyVals);
    }
  }

  public List<RequestDetailModel> getRequestDataModelList(String key) {
    return reqInfoCache.getIfPresent(key);
  }

  public void putInterceptedResponse(String key, InterceptedResponse interceptedResponse) {
    httpMessageCache.put(key, interceptedResponse);
  }

  public InterceptedResponse getInterceptedResponse(String key) {
    return httpMessageCache.getIfPresent(key);
  }

  public void putRule(String key, Rule rule) {
    ruleCache.put(key, rule);
  }

  public Rule getRule(String key) {
    return ruleCache.getIfPresent(key);
  }

  public void addInfoDataModel(String key, InfoDataModel infoDataModel) {
    List<InfoDataModel> vals = infoCache.getIfPresent(key);
    if (vals == null) {
      infoCache.put(key, List.of(infoDataModel));
    } else {
      List<InfoDataModel> copyVals = new ArrayList<>(vals);
      copyVals.add(infoDataModel);
      infoCache.put(key, copyVals);
    }
  }

  public List<InfoDataModel> getInfoData(String key) {
    return infoCache.getIfPresent(key);
  }
}

package com.github.trganda.model.cache;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.trganda.FindSomething;
import com.github.trganda.config.Rules.Rule;
import com.github.trganda.model.RequestDetailModel;
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
  private static final Cache<String, List<RequestDetailModel>> reqInfoCache =
      Caffeine.newBuilder()
          .maximumSize(MAX_SIZE)
          .build();
  private static final Cache<String, Rule> ruleCache =
      Caffeine.newBuilder()
          .maximumSize(MAX_SIZE)
          .build();

  public static void putRequestDataModel(String key, List<RequestDetailModel> requestDataModels) {
    reqInfoCache.put(key, requestDataModels);
  }

  public static void addRequestDataModel(String key, RequestDetailModel requestDataModel) {
    List<RequestDetailModel> vals = reqInfoCache.getIfPresent(key);
    if (vals == null || vals.isEmpty()) {
      putRequestDataModel(key, List.of(requestDataModel));
    } else {
      FindSomething.API.logging().logToOutput(key + " - " + vals.size());
      vals.add(requestDataModel);
      reqInfoCache.put(key, vals);
    }
  }

  public static List<RequestDetailModel> getRequestDataModelList(String key) {
    return reqInfoCache.getIfPresent(key);
  }

  public static void putInterceptedResponse(String key, InterceptedResponse interceptedResponse) {
    httpMessageCache.put(key, interceptedResponse);
  }

  public static InterceptedResponse getInterceptedResponse(String key) {
    return httpMessageCache.getIfPresent(key);
  }

  public static void putRule(String key, Rule rule) {
    ruleCache.put(key, rule);
  }

  public static Rule getRule(String key) {
    return ruleCache.getIfPresent(key);
  }
}

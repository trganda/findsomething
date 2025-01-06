package com.github.trganda.utils.cache;

import burp.api.montoya.proxy.http.InterceptedResponse;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.trganda.config.ConfigManager;
import com.github.trganda.config.Rules;
import com.github.trganda.model.InfoDataModel;
import com.github.trganda.model.RequestDetailModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CachePool {

  private static final int MAX_SIZE = 100000;
  private static final int EXPIRES_IN_HOURS = 3;
  private static final Cache<String, InterceptedResponse> httpMessageCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();
  private static final Cache<String, List<InfoDataModel>> infoCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();
  private static final Cache<String, List<RequestDetailModel>> reqInfoCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();
  private static final Cache<String, List<String>> hostCache =
      Caffeine.newBuilder().maximumSize(MAX_SIZE).build();
  private static final Cache<String, Boolean> duplicateCache =
      Caffeine.newBuilder().expireAfterWrite(EXPIRES_IN_HOURS, TimeUnit.HOURS).build();

  private static CachePool instance;

  public static CachePool getInstance() {
    if (instance == null) {
      instance = new CachePool();
    }
    return instance;
  }

  public void addDuplicate(String key) {
    duplicateCache.put(key, true);
  }

  public boolean isDuplicate(String key) {
    return duplicateCache.getIfPresent(key) != null;
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
      if (!copyVals.stream().anyMatch(v -> v.getMessageId() == requestDataModel.getMessageId())) {
        copyVals.add(requestDataModel);
        reqInfoCache.put(key, copyVals);
      }
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

  public boolean addInfoDataModel(String group, InfoDataModel infoDataModel) {
    List<InfoDataModel> vals = infoCache.getIfPresent(group);
    if (vals == null) {
      infoCache.put(group, List.of(infoDataModel));
      return true;
    } else {
      List<InfoDataModel> copyVals = new ArrayList<>(vals);
      // de-duplicate
      if (copyVals.stream().noneMatch(v -> v.getResult().equals(infoDataModel.getResult()))) {
        copyVals.add(infoDataModel);
        infoCache.put(group, copyVals);
        return true;
      }
    }
    return false;
  }

  public List<InfoDataModel> getInfoData() {
    List<String> groups = ConfigManager.getInstance().getRules().getGroupsName();
    return getAllInfoData(groups);
  }

  private List<InfoDataModel> getAllInfoData(List<String> key) {
    List<InfoDataModel> vals = new ArrayList<>();
    for (String k : key) {
      List<InfoDataModel> temp = infoCache.getIfPresent(k);
      if (temp != null) {
        vals.addAll(temp);
      }
    }

    return vals;
  }

  public void addHost(String host) {
    List<String> vals = hostCache.getIfPresent("Host");
    if (vals == null) {
      hostCache.put("Host", List.of(host));
    } else {
      List<String> copyVals = new ArrayList<>(vals);
      if (!copyVals.contains(host)) {
        copyVals.add(host);
        hostCache.put("Host", copyVals);
      }
    }
  }

  public List<String> getHosts() {
    List<String> vals = hostCache.getIfPresent("Host");
    if (vals == null) {
      vals = new ArrayList<>();
    }
    return vals;
  }

  static {
    CachePool.getInstance().addHost("*");
  }
}

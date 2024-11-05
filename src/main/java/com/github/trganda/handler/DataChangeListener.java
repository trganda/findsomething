package com.github.trganda.handler;

import com.github.trganda.model.InfoDataModel;
import java.util.List;

public interface DataChangeListener {
  void onDataChanged(List<InfoDataModel> data);
}

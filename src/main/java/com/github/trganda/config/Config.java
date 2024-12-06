package com.github.trganda.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Config {
  private List<String> suffixes = new ArrayList<>();
  private List<String> hosts = new ArrayList<>();
  private List<String> status = new ArrayList<>();
}

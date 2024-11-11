package com.github.trganda.cleaner;

import java.util.Arrays;
import java.util.List;
import lombok.Setter;

@Setter
public class Cleaner {
  private List<String> data;
  private final Character[] dirty = new Character[] {'"', '\''};

  public String[] clean() {
    return this.data.stream()
        .map(
            d -> {
              d = removePrefixAndSuffix(d);
              return d;
            })
        .filter(d -> !d.isEmpty())
        .distinct()
        .toArray(String[]::new);
  }

  public String removePrefixAndSuffix(String str) {
    String ret = new String(str.strip());

    List<Character> d = Arrays.asList(dirty);
    
    for (int i = 0; i < str.length(); i++) {
      if (d.contains(str.charAt(i))) {
        ret = str.substring(i + 1);
      } else {
        break;
      }
    }

    str = ret;
    for (int i = str.length() - 1; i >= 0; i--) {
      if (d.contains(str.charAt(i))) {
        ret = str.substring(0, i);
      } else {
        break;
      }
    }

    return ret.strip();
  }
}

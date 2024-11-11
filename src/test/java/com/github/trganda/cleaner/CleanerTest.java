package com.github.trganda.cleaner;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class CleanerTest {

  @Test
  public void testClean() {
    String[] str =
        new String[] {"\"abc\"", "\"\"abc\"\"", "\"abc\'\"", "\"\'abc\"", "abc", "\'\"\"\'"};

    Cleaner cleaner = new Cleaner();
    cleaner.setData(Arrays.asList(str));
    String[] res = cleaner.clean();
    for (String s : res) {
      System.out.println(s);
    }
  }
}

package org.tools.desktop.search;

import org.apache.commons.lang3.StringUtils;

public class Validator {

  public static void validateNotEmpty(String input) {
    validateNotEmpty(input, null);
  }

  public static void validateNotEmpty(String input, String field) {
    if (StringUtils.isBlank(input)) {
      if (StringUtils.isBlank(field)) {
        throw new RuntimeException("Invalid input: " + field);
      }
      throw new RuntimeException("Invalid input");
    }
  }
}

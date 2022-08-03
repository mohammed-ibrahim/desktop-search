package org.tools.desktop.search.document;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonTermsCleaner {

  private static final List<String> TERMS_TO_IGNORE = Arrays.asList(
      "com", "org", "a", "or", "an",
      "to", "www");

  public static List<String> cleanCommonTerms(List<String> input) {
    return input.stream().map(a -> StringUtils.toRootLowerCase(a))
        .filter(a -> !TERMS_TO_IGNORE.contains(a))
        .collect(Collectors.toList());
  }

  public static boolean isCommonTerm(String input) {
    return TERMS_TO_IGNORE.contains(StringUtils.toRootLowerCase(input));
  }
}

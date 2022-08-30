package org.tools.desktop.search.Tokenizer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {

  private static final String SINGLE_SPACE = " ";

  public static void addTokens(List<String> existingTokens, String input) {
    List<String> freshTokens = tokenize(input);
    if (CollectionUtils.isNotEmpty(freshTokens)) {
      existingTokens.addAll(freshTokens);
    }
  }

  public static List<String> tokenize(String input) {
    if (StringUtils.isBlank(input)) {
      return Collections.emptyList();
    }

    String result = input.replaceAll("[^a-zA-Z0-9]+", SINGLE_SPACE);
    result = StringUtils.trim(result).replaceAll(" +", SINGLE_SPACE);
    return Arrays.stream(result.split(SINGLE_SPACE))
        .collect(Collectors.toList());
  }
}

package org.tools.desktop.generic;

import org.apache.commons.lang3.StringUtils;

public class TextCutter {

  public static void main(String[] args) {
    String input = "This is a [looking for] sample text cutter";
    System.out.println(cutText(input, "[", 0, "]", 0));
  }

  public static String cutText(String input, String startSeparator, int startSeparatorIndex,
                               String endSeparator, int endSeparatorIndex) {

    int startOrdinalIndex = StringUtils.ordinalIndexOf(input, startSeparator, startSeparatorIndex);
    int startIndex = input.indexOf(startSeparator, startOrdinalIndex);

    int endOrdinalIndex = StringUtils.ordinalIndexOf(input, endSeparator, endSeparatorIndex);
    int endIndex = input.indexOf(endSeparator, endOrdinalIndex);
    return input.substring(startIndex + 1, endIndex);
  }
}

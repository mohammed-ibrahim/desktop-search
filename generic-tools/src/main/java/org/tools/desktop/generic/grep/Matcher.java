package org.tools.desktop.generic.grep;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Matcher {

  public static void main(String[] args) {
    // all fields applied and should match
    check(matchesFile("/a/b/c/d.txt",
        "d.txt",
        Arrays.asList("d", "d.t"),
        Arrays.asList(".txt"),
        Arrays.asList("gcw")), true, "case1");

    // only matching clause
    check(matchesFile("/a/b/c/d.txt",
        "d.txt",
        Arrays.asList("d", "d.t"),
        Collections.emptyList(),
        Collections.emptyList()), true, "case2");

    // only extension clause
    check(matchesFile("/a/b/c/d.txt",
        "d.txt",
        Collections.emptyList(),
        Arrays.asList(".txt", "txt", "xt"),
        Collections.emptyList()), true, "case3");

    // only exclusion clause
    check(matchesFile("/a/b/c/d.txt",
        "d.txt",
        Collections.emptyList(),
        Collections.emptyList(),
        Arrays.asList("/b/c", "/d.txt", "txt")), false, "case3");
  }

  private static void check(boolean a, boolean b, String caseName) {
    if (a != b) {
      throw new RuntimeException("Doesn't match: " + caseName);
    } else {
      System.out.println("OK - " + caseName);
    }
  }

  public static boolean matchesFile(
      String fullPath,
      String fileName,
      List<String> fileNameMustContain,
      List<String> fileExtension,
      List<String> filePathMustNotContain) {

    for (String mustMatchTerm : fileNameMustContain) {
      if (!StringUtils.containsIgnoreCase(fileName, mustMatchTerm)) {
        return false;
      }
    }

    for (String mustEndWithTerm : fileExtension) {
      if (!StringUtils.endsWithIgnoreCase(fileName, mustEndWithTerm)) {
        return false;
      }
    }

    for (String mustNotMatchTerm : filePathMustNotContain) {
      if (StringUtils.containsIgnoreCase(fullPath, mustNotMatchTerm)) {
        return false;
      }
    }

    return true;
  }
}

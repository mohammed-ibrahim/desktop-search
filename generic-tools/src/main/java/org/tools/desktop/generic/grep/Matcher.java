package org.tools.desktop.generic.grep;

import org.apache.commons.lang3.StringUtils;
import org.tools.desktop.generic.grep.model.FileLookupFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Matcher {

  public static void main(String[] args) {
    // all fields applied and should match
    check(testMatchesFile("/a/b/c/d.txt",
        "d.txt",
        Arrays.asList("d", "d.t"),
        Arrays.asList(".txt"),
        Arrays.asList("gcw")), true, "case1");

    // only matching clause
    check(testMatchesFile("/a/b/c/d.txt",
        "d.txt",
        Arrays.asList("d", "d.t"),
        Collections.emptyList(),
        Collections.emptyList()), true, "case2");

    // only extension clause
    check(testMatchesFile("/a/b/c/d.txt",
        "d.txt",
        Collections.emptyList(),
        Arrays.asList(".txt", "txt", "xt"),
        Collections.emptyList()), true, "case3");

    // only exclusion clause
    check(testMatchesFile("/a/b/c/d.txt",
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

  private static boolean testMatchesFile(
      String fullPath,
      String fileName,
      List<String> fileNameMustContain,
      List<String> fileExtension,
      List<String> filePathMustNotContain) {

    FileLookupFilter fileLookupFilter = new FileLookupFilter(fileExtension, fileNameMustContain, filePathMustNotContain);
    return matchesFile(fullPath, fileName, fileLookupFilter);
  }

  public static boolean matchesFile(
      String fullPath,
      String fileName,
      FileLookupFilter fileLookupFilter) {

    for (String mustMatchTerm : fileLookupFilter.getFileNameMustContain()) {
      if (!StringUtils.containsIgnoreCase(fileName, mustMatchTerm)) {
        return false;
      }
    }

    for (String mustEndWithTerm : fileLookupFilter.getFileExtension()) {
      if (!StringUtils.endsWithIgnoreCase(fileName, mustEndWithTerm)) {
        return false;
      }
    }

    for (String mustNotMatchTerm : fileLookupFilter.getFilePathMustNotContain()) {
      if (StringUtils.containsIgnoreCase(fullPath, mustNotMatchTerm)) {
        return false;
      }
    }

    return true;
  }
}

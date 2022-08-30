package org.tools.desktop.search.bookmark;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookMarkParser {

  public static List<BookMark> loadBookMarks(File bookmarkHtmlFile) {
    try {
      return safeLoadBookMarks(bookmarkHtmlFile);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public static List<BookMark> safeLoadBookMarks(File bookmarkHtmlFile) throws Exception {
    String fileContents = FileUtils.readFileToString(bookmarkHtmlFile, StandardCharsets.UTF_8);
    List<String> lines = Arrays.asList(fileContents.split("\n"));

    List<BookMark> bookmarks = lines.stream()
        .filter(line -> StringUtils.containsIgnoreCase(line, "HREF="))
        .map(line -> composeBookMark(line))
        .collect(Collectors.toList());

//    bookmarks.forEach(f -> System.out.println(f.getLink() + " :::: " + f.getTitle()));
    return bookmarks;
  }

  private static BookMark composeBookMark(String line) {
    BookMark bookMark = new BookMark();

    bookMark.setLink(getLink(line));
    bookMark.setTitle(getTitle(line));

    return bookMark;
  }

  private static String getTitle(String line) {
    int startIndex = line.indexOf("\">");
    int endIndex = line.indexOf("</A>");

    if (startIndex == -1
      || endIndex == -1
      || ((startIndex+2) >= endIndex)) {
      throw new RuntimeException("Unable to parse title");
    }

    return line.substring(startIndex + 2, endIndex);
  }

  private static String getLink(String line) {

    String result = Arrays.stream(line.split(" "))
        .filter(a -> a.contains("HREF="))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("HREF not found"));

    return result
        .replace("HREF=\"", "")
        .replace("\"", "");
  }

  public static void main(String[] args) {
    List<BookMark> bookMarks = loadBookMarks(new File(args[0]));
  }
}

package org.tools.desktop.generic.grep.stream;

import org.apache.commons.io.FileUtils;
import org.tools.desktop.generic.grep.GrepMain;
import org.tools.desktop.generic.grep.Matcher;
import org.tools.desktop.generic.grep.model.LineLookupFilter;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.tools.desktop.generic.grep.GrepMain.NEW_LINE_CHAR;


public class SmallFileStreamer implements FileStreamer {
  @Override
  public void processFile(Path file, BufferedWriter bufferedWriter, LineLookupFilter lineLookupFilter) {
    try {
      String str = FileUtils.readFileToString(file.toFile(), StandardCharsets.UTF_8);
      List<String> list = Arrays.asList(str.split(NEW_LINE_CHAR));

      for (String line : list) {
        if (Matcher.matchesLine(line, lineLookupFilter)) {
          bufferedWriter.write(line);
          bufferedWriter.newLine();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}

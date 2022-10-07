package org.tools.desktop.generic.grep.stream;

import org.tools.desktop.generic.grep.Matcher;
import org.tools.desktop.generic.grep.model.LineLookupFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.nio.file.Path;

import static org.tools.desktop.generic.grep.GrepMain.NEW_LINE_CHAR;

public class LargeFileStreamer implements FileStreamer {
  @Override
  public void processFile(Path file, BufferedWriter bufferedWriter, LineLookupFilter lineLookupFilter) {

    try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
      String line = null;
      int lineNum = 0;

      while ((line = reader.readLine()) != null) {
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

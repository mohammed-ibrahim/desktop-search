package org.tools.desktop.generic.grep;

import org.tools.desktop.generic.grep.model.FileLookupFilter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FilesCollector {

  public static List<Path> getFiles(
      String srcDir,
      FileLookupFilter fileLookupFilter) {

    try {

      List<Path> files = Files.find(Paths.get(srcDir), Integer.MAX_VALUE,
          (filePath, fileAttr) -> fileAttr.isRegularFile())
          .collect(Collectors.toList());

      List<Path> filteredFiles = files.stream()
          .filter(file ->
              Matcher.matchesFile(file.toString(),
                  file.getFileName().toString(), fileLookupFilter))
          .collect(Collectors.toList());

      return filteredFiles;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

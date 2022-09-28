package org.tools.desktop.generic.grep;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FilesCollector {

  public static List<Path> getFiles(
      String srcDir,
      List<String> fileNameMustContain,
      List<String> fileExtension,
      List<String> filePathMustNotContain) {

    try {

      List<Path> files = Files.find(Paths.get(srcDir), Integer.MAX_VALUE,
          (filePath, fileAttr) -> fileAttr.isRegularFile())
          .collect(Collectors.toList());

      List<Path> filteredFiles = files.stream()
          .filter(file ->
              Matcher.matchesFile(file.toString(),
                  file.getFileName().toString(),
                  fileNameMustContain,
                  fileExtension,
                  filePathMustNotContain))
          .collect(Collectors.toList());

      return filteredFiles;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

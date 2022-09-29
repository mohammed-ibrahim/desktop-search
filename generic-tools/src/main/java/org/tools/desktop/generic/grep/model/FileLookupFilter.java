package org.tools.desktop.generic.grep.model;

import java.util.List;

public class FileLookupFilter {

  private List<String> fileExtension;
  private List<String> fileNameMustContain;
  private List<String> filePathMustNotContain;

  public FileLookupFilter(List<String> fileExtension,
                          List<String> fileNameMustContain,
                          List<String> filePathMustNotContain) {
    this.fileExtension = fileExtension;
    this.fileNameMustContain = fileNameMustContain;
    this.filePathMustNotContain = filePathMustNotContain;
  }

  public List<String> getFileExtension() {
    return fileExtension;
  }

  public List<String> getFileNameMustContain() {
    return fileNameMustContain;
  }

  public List<String> getFilePathMustNotContain() {
    return filePathMustNotContain;
  }
}

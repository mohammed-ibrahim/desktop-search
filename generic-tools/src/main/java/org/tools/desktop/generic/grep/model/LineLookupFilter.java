package org.tools.desktop.generic.grep.model;

import java.util.List;

public class LineLookupFilter {
  private List<String> lineMustContain;
  private List<String> lineMustNotContain;

  public LineLookupFilter(List<String> lineMustContain, List<String> lineMustNotContain) {
    this.lineMustContain = lineMustContain;
    this.lineMustNotContain = lineMustNotContain;
  }

  public List<String> getLineMustContain() {
    return lineMustContain;
  }

  public List<String> getLineMustNotContain() {
    return lineMustNotContain;
  }
}

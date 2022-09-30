package org.tools.desktop.generic.grep.stream;

import org.tools.desktop.generic.grep.model.LineLookupFilter;

import java.io.BufferedWriter;
import java.nio.file.Path;

public interface FileStreamer {

  void processFile(Path file, BufferedWriter bufferedWriter, LineLookupFilter lineLookupFilter);
}

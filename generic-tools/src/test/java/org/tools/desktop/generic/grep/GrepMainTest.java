package org.tools.desktop.generic.grep;


import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class GrepMainTest {

  private Path getTempDirectoryInstance() {
    Path newDir = Paths.get(System. getProperty("java.io.tmpdir"), UUID.randomUUID().toString().substring(0, 10), "test");
    newDir.toFile().mkdirs();
    return newDir;
  }

  private void saveFile(Path path, String contents) {
    try {
      FileUtils.writeStringToFile(path.toFile(), contents, StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test() throws Exception {
    Path temp = getTempDirectoryInstance();
    saveFile(Paths.get(temp.toString(), "file1.txt"), "one two three");
    saveFile(Paths.get(temp.toString(), "file2.txt"), "one two unexpected token three"); //rejected for line must not contain "unexpected token"
    saveFile(Paths.get(temp.toString(), "file3.log"), "one two three");                  //rejected for different extension.
    saveFile(Paths.get(temp.toString(), "file4.txt"), "one two three unexpected_token"); //rejected for unexpected_token
    saveFile(Paths.get(temp.toString(), "file5.txt"), "one two three gz ux");            //rejected for containing 'ux' 'gz'
    saveFile(Paths.get(temp.toString(), "file_name_must_not_contain.txt"), "one two three"); //rejected for filepath must not contain 'must_not_contain'

    Path resultsFile = Paths.get(temp.toString(), "results.log");
    GrepMain.startProcessing(
        temp.toString(),
        null,
        resultsFile.toString(),
        Collections.singletonList("txt"),
        Collections.emptyList(),
        Collections.singletonList("must_not_contain"),
        Arrays.asList("one two", "three"),
        Arrays.asList("unexpected token", "unexpected_token", "gz", "ux"),
        false
    );


    String contents = FileUtils.readFileToString(resultsFile.toFile(), StandardCharsets.UTF_8.name());
    Assert.assertEquals(contents, "one two three" + GrepMain.NEW_LINE_CHAR);
    /**
     * String srcDir,
     *       String outputDir,
     *       String outputFile,
     *       List<String> fileExtension,
     *       List<String> fileNameMustContain,
     *       List<String> filePathMustNotContain,
     *       List<String> lineMustContain,
     *       List<String> lineMustNotContain,
     *       boolean addTimestampToResultantFileName
     */
  }
}
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
import java.util.List;
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
  public void canApplyGenericFilters() throws Exception {
    Path temp = getTempDirectoryInstance();
    saveFile(Paths.get(temp.toString(), "file1.txt"), "one two three");
    saveFile(Paths.get(temp.toString(), "file2.txt"), "one two unexpected token three"); //rejected for line must not contain "unexpected token"
    saveFile(Paths.get(temp.toString(), "file3.log"), "one two three");                  //rejected for different extension.
    saveFile(Paths.get(temp.toString(), "file4.txt"), "one two three unexpected_token"); //rejected for unexpected_token
    saveFile(Paths.get(temp.toString(), "file5.txt"), "one two three gz ux");            //rejected for containing 'ux' 'gz'
    saveFile(Paths.get(temp.toString(), "file_name_must_not_contain.txt"), "one two three"); //rejected for filepath must not contain 'must_not_contain'

    Path resultsFile = Paths.get(temp.toString(), "results.log");
    String srcDir = temp.toString();
    String outputDir = null;
    String outputFile = resultsFile.toString();
    List<String> fileExtension = Collections.singletonList("txt");
    List<String> fileNameMustContain = Collections.emptyList();
    List<String> filePathMustNotContain = Collections.singletonList("must_not_contain");
    List<String> lineMustContain = Arrays.asList("one two", "three");
    List<String> lineMustNotContain = Arrays.asList("unexpected token", "unexpected_token", "gz", "ux");

    GrepMain.startProcessing(srcDir, outputDir, outputFile, fileExtension, fileNameMustContain, filePathMustNotContain, lineMustContain, lineMustNotContain, false);

    String contents = FileUtils.readFileToString(resultsFile.toFile(), StandardCharsets.UTF_8.name());
    Assert.assertEquals(contents, "one two three" + GrepMain.NEW_LINE_CHAR);

  }

  @Test
  public void canIgnoreFilePaths() throws Exception {
    Path temp = getTempDirectoryInstance();
    String sampleContent = "one two three";
    saveFile(Paths.get(temp.toString(), "file1.txt"), sampleContent);

    Path newSubDir = Paths.get(temp.toString(), "dir_to_ignore", "sub_folder");
    newSubDir.toFile().mkdirs();
    saveFile(Paths.get(newSubDir.toString(), "file2.txt"), sampleContent);

    Path resultsFile = Paths.get(temp.toString(), "results.log");
    String srcDir = temp.toString();
    String outputDir = null;
    String outputFile = resultsFile.toString();
    List<String> fileExtension = Collections.singletonList("txt");
    List<String> fileNameMustContain = Collections.emptyList();
    List<String> filePathMustNotContain = Collections.singletonList("dir_to_ignore" + System.getProperty("file.separator") + "sub_folder");
    List<String> lineMustContain = Collections.singletonList(sampleContent);
    List<String> lineMustNotContain = Collections.emptyList();

    GrepMain.startProcessing(srcDir, outputDir, outputFile, fileExtension, fileNameMustContain, filePathMustNotContain, lineMustContain, lineMustNotContain, false);

    String contents = FileUtils.readFileToString(resultsFile.toFile(), StandardCharsets.UTF_8.name());
    Assert.assertEquals(contents, sampleContent + GrepMain.NEW_LINE_CHAR);
  }

  @Test
  public void canGivePrecedenceToLineMustNotContainEvenThoughLineMatchesPattern() throws Exception {
    Path temp = getTempDirectoryInstance();
    String sampleContent = "one two three";
    String unexpectedContent = "unexpected content";
    saveFile(Paths.get(temp.toString(), "file1.txt"), sampleContent);
    saveFile(Paths.get(temp.toString(), "file2.txt"),  sampleContent + " " + unexpectedContent);

    Path resultsFile = Paths.get(temp.toString(), "results.log");
    String srcDir = temp.toString();
    String outputDir = null;
    String outputFile = resultsFile.toString();
    List<String> fileExtension = Collections.emptyList();
    List<String> fileNameMustContain = Collections.emptyList();
    List<String> filePathMustNotContain = Collections.singletonList("dir_to_ignore" + System.getProperty("file.separator") + "sub_folder");
    List<String> lineMustContain = Collections.singletonList(sampleContent);
    List<String> lineMustNotContain = Collections.singletonList(unexpectedContent);

    GrepMain.startProcessing(srcDir, outputDir, outputFile, fileExtension, fileNameMustContain, filePathMustNotContain, lineMustContain, lineMustNotContain, false);

    String contents = FileUtils.readFileToString(resultsFile.toFile(), StandardCharsets.UTF_8.name());
    Assert.assertEquals(contents, sampleContent + GrepMain.NEW_LINE_CHAR);
  }
}
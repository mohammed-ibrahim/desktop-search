package org.tools.desktop.generic.grep;

import org.apache.commons.lang3.StringUtils;
import org.tools.desktop.generic.AppLog;
import org.tools.desktop.generic.grep.model.FileLookupFilter;
import org.tools.desktop.generic.grep.model.LineLookupFilter;
import org.tools.desktop.generic.grep.stream.FileStreamer;
import org.tools.desktop.generic.grep.stream.LargeFileStreamer;
import org.tools.desktop.generic.grep.stream.SmallFileStreamer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GrepMain {

  public static String NEW_LINE_CHAR = "\n";

  private static final long MAX_FILE_SIZE_IN_MB_THRESHOLD = 30l;
  private static String SRC_DIR = "-srcDir";
  private static String OUTPUT_DIR = "-outputDir";
  private static String OUTPUT_FILE = "-outputFile";
  private static String FILE_EXTENSION = "-fileExtension";
  private static String FILENAME_MUST_CONTAIN = "-fileNameMustContain";
  private static String EXCLUDE_IN_PATH = "-filePathMustNotContain";

  private static String LINE_MUST_CONTAIN = "-lineMustContain";
  private static String LINE_MUST_NOT_CONTAIN = "-lineMustNotContain";

  private static boolean PARAM_REQUIRED = true;
  private static boolean PARAM_OPTIONAL = false;

  public static void main(String[] argzs) {
    if (argzs == null || argzs.length == 0) {
      AppLog.print("Need to supply params");
      System.exit(1);
    }

    List<String> args = Arrays.asList(argzs);
    String srcDir = getSingleParam(args, SRC_DIR, PARAM_REQUIRED);
    String outputDir = getSingleParam(args, OUTPUT_DIR, PARAM_OPTIONAL);
    String outputFile = getSingleParam(args, OUTPUT_FILE, PARAM_OPTIONAL);

    if (outputDir == null && outputFile == null) {
      AppLog.print("outputDir or outputFile must be specified.");
      System.exit(1);
    }

    String resultantFile = null;
    if (outputDir != null) {
      resultantFile = generateFileNameIfSrcDirProvided(outputDir);
    } else if (outputFile != null) {
      resultantFile = getTimeStampedFileName(outputFile);
    }

    List<String> fileExtension = getMultiParam(args, FILE_EXTENSION, PARAM_OPTIONAL);

    List<String> fileNameMustContain = getMultiParam(args, FILENAME_MUST_CONTAIN, PARAM_OPTIONAL);
    List<String> filePathMustNotContain = getMultiParam(args, EXCLUDE_IN_PATH, PARAM_OPTIONAL);

    List<String> lineMustContain = getMultiParam(args, LINE_MUST_CONTAIN, PARAM_REQUIRED);
    List<String> lineMustNotContain = getMultiParam(args, LINE_MUST_NOT_CONTAIN, PARAM_OPTIONAL);
    LineLookupFilter lineLookupFilter = new LineLookupFilter(lineMustContain, lineMustNotContain);

    FileLookupFilter fileLookupFilter = new FileLookupFilter(
        fileExtension, fileNameMustContain, filePathMustNotContain);

    List<Path> files = FilesCollector.getFiles(srcDir, fileLookupFilter);
    AppLog.print("Number of files found: " + files.size());

    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultantFile))) {
      for (Path file : files) {
        processFile(file, bufferedWriter, lineLookupFilter);
      }
    } catch (Exception e) {
      AppLog.print("Error occurred: " + e.getMessage());
      e.printStackTrace();
    }

  }

  private static final long  MEGABYTE = 1024L * 1024L;

  public static long bytesToMeg(long bytes) {
    return bytes / MEGABYTE ;
  }

  private static void processFile(Path file, BufferedWriter bufferedWriter, LineLookupFilter lineLookupFilter) {
    try {
      long numBytes = Files.size(file);
      long mb = bytesToMeg(numBytes);

      FileStreamer fileStreamer = null;
      if (mb >= MAX_FILE_SIZE_IN_MB_THRESHOLD) {
        fileStreamer = new LargeFileStreamer();
      } else {
        fileStreamer = new SmallFileStreamer();
      }

      fileStreamer.processFile(file, bufferedWriter, lineLookupFilter);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String generateFileNameIfSrcDirProvided(String outputDir) {
    String fileName = String.format("results-%s.log", generateTimeStampForFileName());
    return Paths.get(outputDir, fileName).toString();
  }

  private static String getTimeStampedFileName(String fullPath) {
    return String.format("%s-%s.log", fullPath, generateTimeStampForFileName());
  }

  private static String generateTimeStampForFileName() {
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy-HH-mm-ss");
    return dateFormat.format(date);
  }

  private static String getSingleParam(List<String> args,
                                       String argumentName,
                                       boolean isRequired) {

    List<String> multiParam = getMultiParam(args, argumentName, isRequired);

    if (multiParam.isEmpty()) {
      return null;
    }

    return multiParam.get(0);
  }

  private static List<String> getMultiParam(
      List<String> args,
      String argumentName,
      boolean isRequired) {

    List<String> results = args.stream()
        .filter(x -> x.startsWith(argumentName))
        .map(x -> x.substring(argumentName.length() + 1))
        .filter(x -> StringUtils.isNotBlank(x))
        .collect(Collectors.toList());

    if (isRequired && results.isEmpty()) {
      AppLog.print("Argument cannot be empty: " + argumentName);
      System.exit(1);
    }

    return results;
  }
}

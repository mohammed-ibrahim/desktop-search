package org.tools.desktop.generic.grep;

import org.apache.commons.lang3.StringUtils;
import org.tools.desktop.generic.AppLog;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GrepMain {

  private static String SRC_DIR = "-srcDir";
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
    List<String> fileExtension = getMultiParam(args, FILE_EXTENSION, PARAM_OPTIONAL);

    List<String> fileNameMustContain = getMultiParam(args, FILENAME_MUST_CONTAIN, PARAM_OPTIONAL);
    List<String> filePathMustNotContain = getMultiParam(args, EXCLUDE_IN_PATH, PARAM_OPTIONAL);

    List<String> lineMustContain = getMultiParam(args, LINE_MUST_CONTAIN, PARAM_REQUIRED);
    List<String> lineMustNotContain = getMultiParam(args, LINE_MUST_NOT_CONTAIN, PARAM_OPTIONAL);

    FilesCollector.getFiles(
        srcDir,
        fileNameMustContain,
        fileExtension,
        filePathMustNotContain);
    /**
     * Step 1 :: collect files
     * Step 2 :: search files
     * Step 3 :: dump results
     * Step 4 #1 snd #3 should happen in sync
     */

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

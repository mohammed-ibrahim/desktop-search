package org.tools.desktop.generic;

import org.apache.commons.lang3.StringUtils;

public class Slugify {
  public static void main(String[] args) {

    if (args == null || args.length == 0) {
      System.out.println("No arguments supplied");
      return;
    }

    String joined = StringUtils.join(args, " ")
        .replaceAll("-+", "-");
    System.out.println(joined);
  }
}

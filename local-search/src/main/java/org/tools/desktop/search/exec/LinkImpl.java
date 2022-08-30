package org.tools.desktop.search.exec;


import java.awt.*;
import java.net.URI;

public class LinkImpl {

  public static void openLink(String link) {
    try {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(new URI(link));
      } else {
        throw new RuntimeException("Desktop not supported");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

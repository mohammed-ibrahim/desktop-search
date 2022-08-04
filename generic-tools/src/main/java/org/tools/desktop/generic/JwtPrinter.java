package org.tools.desktop.generic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.Base64;

public class JwtPrinter {

  public static void main(String[] args) throws Exception {
    String jwt = (String) Toolkit.getDefaultToolkit()
        .getSystemClipboard().getData(DataFlavor.stringFlavor);

    if (StringUtils.isBlank(jwt)) {
      System.out.println("Nothing in clipboard");
      return;
    }

    System.out.println("\n\nCopied jwt: " + jwt + "\n\n");
    String payload = getPayload(jwt);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(payload);

    String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    System.out.println(json);
  }

  private static String getPayload(String jwt) {
    String[] parts = jwt.split("\\.");

    if (parts.length < 2) {
      throw new RuntimeException("Jwt is Invalid");
    }

    byte[] decoded = Base64.getDecoder().decode(parts[1]);
    return new String(decoded);
  }
}

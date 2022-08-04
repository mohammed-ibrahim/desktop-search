package org.tools.desktop.generic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.Base64;

public class JwtPrinter {

  public static void main(String[] args) {
    try {
      printJwt(args);
    } catch (Exception e) {
      System.out.println("The was an ERROR: " + e.getMessage());
    }
  }

  private static void printJwt(String[] args) throws Exception {
    String jwt = args.length == 0 ? loadJwtFromClipboard() : args[0];

    System.out.println("\n\nFound jwt: " + jwt + "\n\n");
    String payload = getPayload(jwt);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(payload);

    String json = objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(jsonNode);

    System.out.println(json);
  }

  private static String loadJwtFromClipboard() throws Exception {
    String jwt = (String) Toolkit.getDefaultToolkit()
        .getSystemClipboard().getData(DataFlavor.stringFlavor);

    if (StringUtils.isBlank(jwt)) {
      throw new RuntimeException("Nothing in clipboard as well!");
    }

    return jwt;
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

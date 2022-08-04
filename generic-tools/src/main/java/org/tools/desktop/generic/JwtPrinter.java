package org.tools.desktop.generic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public class JwtPrinter {


  private static final String EMPTY_CLIPBOARD = "nothing.in.clipboard";
  private static String INVALID_JWT = "invalid.jwt";
  private static final AtomicReference<String> jwtRef = new AtomicReference<>();

  private static final String SUCCESS_TEMPLATE = "\n---------------------------------------------------------\n" +
      "Jwt: %s\n" +
      "\n" +
      "%s\n" +
      "---------------------------------------------------------\n";

  private static final String FAILED_TEMPLATE = "---------------------------------------------------------\n" +
      "Failed\n" +
      "\n" +
      "%s\n" +
      "\n" +
      "---------------------------------------------------------";

  private static final String FAILED_WITH_JWT_TEMPLATE = "---------------------------------------------------------\n" +
      "Failed\n" +
      "\n" +
      "Jwt\n" +
      "%s\n" +
      "\n" +
      "Reason\n" +
      "%s\n" +
      "\n" +
      "---------------------------------------------------------";

  public static void main(String[] args) {
    try {
      Pair<String, String> jwtJson = getJwtJson(args);
      System.out.println(String.format(SUCCESS_TEMPLATE, jwtJson.getLeft(), jwtJson.getRight()));
    } catch (Exception e) {

      String jwt = jwtRef.get();
      if (StringUtils.isNotBlank(jwt)) {
        System.out.println(String.format(FAILED_WITH_JWT_TEMPLATE, jwt, e.getMessage()));
      } else {
        System.out.println(String.format(FAILED_TEMPLATE, e.getMessage()));
      }
    }
  }

  private static Pair<String, String> getJwtJson(String[] args) throws Exception {
    String jwt = args.length == 0
        ? loadJwtFromClipboard()
        : args[0];

    jwtRef.set(jwt);
    String payload = getPayload(jwt);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(payload);

    String json = objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(jsonNode);

    return Pair.of(jwt, json);
  }

  private static String loadJwtFromClipboard() throws Exception {
    String jwt = (String) Toolkit.getDefaultToolkit()
        .getSystemClipboard().getData(DataFlavor.stringFlavor);

    if (StringUtils.isBlank(jwt)) {
      throw new RuntimeException(EMPTY_CLIPBOARD);
    }

    return jwt;
  }

  private static String getPayload(String jwt) {
    String[] parts = jwt.split("\\.");

    if (parts.length < 2) {
      throw new RuntimeException(INVALID_JWT);
    }

    byte[] decoded = Base64.getDecoder().decode(parts[1]);
    return new String(decoded);
  }
}

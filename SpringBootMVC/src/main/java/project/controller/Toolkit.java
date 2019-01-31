
package project.controller;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class Toolkit {
  public static String decode(String authenticationHeaderText) {
    String encodedText = authenticationHeaderText.substring(5).trim();
    byte[] credDecode = Base64.getDecoder().decode(encodedText);
    return new String(credDecode, StandardCharsets.UTF_8);
  }

  public static String getUserName(String authenticationHeaderText) {
    return decode(authenticationHeaderText).split(":", 2)[0];
  }

  public static String getPassword(String authenticationHeaderText) {
    return decode(authenticationHeaderText).split(":", 2)[1];
  }
}

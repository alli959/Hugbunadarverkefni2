
package project.controller;

import org.springframework.data.repository.CrudRepository;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;


public class Toolkit {
  // used to decode the Authorization header
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

  // If you have a List<Long> of ids you can call
  //      Toolkit.idsToEntities(list, repository);
  // and get the entities as List<Entity>
  public static <Repository extends CrudRepository<Entity, Long> , Entity> List<Entity> idsToEntities(List<Long> teamIds, Repository repository) {
    return teamIds
      .stream()
      .map(id -> repository.findById(id).get())
      .collect(Collectors.toList());
  }
}

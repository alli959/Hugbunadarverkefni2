
package project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import project.persistence.entities.*;
import project.persistence.repositories.*;

/**

    REQUIRES Stats.java

    Close to being done, but we however need to put in some handy
    variables into 'Stats.java' that are calculated from
    the input data.

    Todo:
    Use RequestHeaders to do this


**/

@CrossOrigin
@RestController
public class PlayerController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private GameRepository gameRepository;

  // Method: localhost:8080/user/savePlayer?playerNr[int]&teamId=[long]&name=[string]&playerPos=[string]
  // Return: The player created as JSON
  @RequestMapping(value = "/user/savePlayer", method = RequestMethod.GET)
  public Player playerAddPost(
    @RequestHeader("Authorization") String authString,
    @RequestParam(required=false) Long id,
    @RequestParam Long playerNr,
    @RequestParam Long teamId,
    @RequestParam String name,
    @RequestParam String playerPos
  ){
    User user = userRepository.findById(Toolkit.getUserName(authString)).get();
    Team team = teamRepository.findById(teamId).get();
    Player newPlayer = new Player();
    if (id != null) newPlayer = playerRepository.findById(id).get();
    newPlayer.setTeamId(teamId);
    newPlayer.setPlayerPos(playerPos);
    newPlayer.setPlayerNr(playerNr);
    newPlayer.setName(name);
    newPlayer = playerRepository.save(newPlayer);
    team.addPlayer(newPlayer);
    teamRepository.save(team);
    return newPlayer;
  }

  // Method: localhost:8080/user/getPlayer?id=[long]
  // Return: "Success" if was successful, otherwise not a 4xx/5xx response
  @RequestMapping(value = "/user/removePlayer", method = RequestMethod.GET)
  public String removePlayer(
      @RequestParam Long id
  ) {
    Player player = playerRepository.findById(id).get();
    Team team = teamRepository.findById(player.getTeamId()).get();
    team.removePlayer(id);
    playerRepository.deleteById(id);
    return "Success";
  }

  // Method: localhost:8080/user/getPlayer?id=[long]
  // Return: Player with id as id as JSON
  @RequestMapping(value = "/user/getPlayer", method = RequestMethod.GET)
  public Player playerGetFromName(
    @RequestParam Long id
  ){
    return playerRepository.findById(id).get();
  }

  // Method: localhost:8080/user/getStatsForPlayer?playerId=[long]
  // Return: Stats object of the player with playerId as JSON
  @RequestMapping(value = "/user/getStatsForPlayer", method = RequestMethod.GET)
  public Stats getStatsForPlayer(
    @RequestParam Long playerId
  ) {
    Player player = playerRepository.findById(playerId).get();
    List<Long> gameIds = player.getGamesPlayed();
    List<Game> games = Toolkit.idsToEntities(gameIds, gameRepository);
    Stats stats = new Stats();
    for (Game game : games)
      stats.addData(game.compileStats(playerId));
    stats.recalculateStats();
    return stats;
  }
}

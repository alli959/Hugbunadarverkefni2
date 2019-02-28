
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

  // Method: localhost:8080/user/createPlayer?playerNr[int]&teamId=[long]&name=[string]&playerPos=[string]
  // Return: The player created as JSON
  @RequestMapping(value = "/user/createPlayer", method = RequestMethod.GET)
  public Player playerAddPost(
    @RequestHeader("Authorization") String authString,
    @RequestParam Long playerNr,
    @RequestParam Long teamId,
    @RequestParam String name,
    @RequestParam String playerPos
  ){
    User user = userRepository.findById(Toolkit.getUserName(authString)).get();
    Team team = teamRepository.findById(teamId).get();
    Player newPlayer = new Player();
    newPlayer.setTeamId(teamId);
    newPlayer.setPlayerPos(playerPos);
    newPlayer.setPlayerNr(playerNr);
    newPlayer.setName(name);
    newPlayer = playerRepository.save(newPlayer);
    team.addPlayer(newPlayer);
    teamRepository.save(team);
    return newPlayer;
  }

  // Method: localhost:8080/user/getPlayer?playerId=[long]
  // Return: Player with id as playerId as JSON
  @RequestMapping(value = "/user/getPlayer", method = RequestMethod.GET)
  public Player playerGetFromName(
    @RequestParam Long playerId
  ){
    return playerRepository.findById(playerId).get();
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

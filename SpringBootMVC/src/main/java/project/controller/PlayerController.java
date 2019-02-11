
package project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import project.persistence.entities.*;
import project.persistence.repositories.*;

/**

    REQUIRES PlayerStats.java

    Close to being done, but we however need to put in some handy
    variables into 'PlayerStats.java' that are calculated from
    the input data.

    Todo:
    Use RequestHeaders to do this


**/


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

  // Not reccomended to use, better getTeam
  @RequestMapping(value = "/user/getTeam", method = RequestMethod.GET)
  public Team playerAddGet(@RequestParam Long teamId){
    return teamRepository.findById(teamId).get();
  }

  // Done
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

  // Done
  @RequestMapping(value = "/user/getPlayer", method = RequestMethod.GET)
  public Player playerGetFromName(
    @RequestParam Long playerId
  ){
    return playerRepository.findById(playerId).get();
  }

  // Done
  @RequestMapping(value = "/user/getStatsForPlayer", method = RequestMethod.GET)
  public PlayerStats getPlayerStatsForPlayer(
    @RequestParam Long playerId
  ) {
    Player player = playerRepository.findById(playerId).get();
    List<Long> gameIds = player.getGamesPlayed();
    List<Game> games = Toolkit.idsToEntities(gameIds, gameRepository);
    int[][] totalStats = new int[GameEvent.N_GAME_EVENTS][GameEvent.N_LOCATIONS];
    for (Game game : games) {
      int[][] stats = game.compileStats(playerId);
      for (int i = 0; i < stats.length; i++)
        for (int j = 0; j < stats[i].length; i++)
          totalStats[i][j] += stats[i][j];
    }
    return new PlayerStats(totalStats, playerId);
  }
}

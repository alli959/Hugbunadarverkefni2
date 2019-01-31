
package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.*;
import project.persistence.repositories.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerController {

  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private GameRepository gameRepository;

  public List<Game> listOfLongToGames(List<Long> gameIds) {
    return gameIds
    .stream()
    .map(id -> gameRepository.findById(id).get())
    .collect(Collectors.toList());
  }

  @RequestMapping(value = "/user/team/{teamId}/player", method = RequestMethod.GET)
  public Team playerAddGet(@PathVariable Long teamId){
    return teamRepository.findById(teamId).get();
  }

  @RequestMapping(value = "/user/team/{teamId}/player", method = RequestMethod.POST)
  public String playerAddPost(@ModelAttribute("playerAdd") Player player,
  HttpSession session,
  @PathVariable Long teamId,
  Model model){
    User loggedInUser = (User)session.getAttribute("login");
    Team team = teamRepository.findById(teamId).get();
    if(loggedInUser != null) {
      playerRepository.save(player);
      model.addAttribute("msg", loggedInUser.getName());
      model.addAttribute("teamId", teamId);
      model.addAttribute("playerNo", team.getPlayers().size());
      model.addAttribute("players", team.getPlayers());
      model.addAttribute("playerAdd", new Player());
      return "player/Player";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/team/{teamId}/player/{playerId}", method = RequestMethod.GET)
  public String playerGetFromName(@PathVariable Long playerId,
  @PathVariable Long teamId,
  HttpSession session,
  Model model){
    Player player = playerRepository.findById(playerId).get();
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/player/{playerId}/getStats", method = RequestMethod.GET)
  public PlayerStats getPlayerStatsForPlayer(
    @PathVariable Long playerId
  ) {
    Player player = playerRepository.findById(playerId).get();
    List<Long> gameIds = player.getGamesPlayed();
    List<Game> games = listOfLongToGames(gameIds);
    int[][] totalStats;
    for (Game game : game) {
      int[][] stats = game.compileStats(playerId)
      for (int i = 0; i < stats.length; i++)
        for (int j = 0; j < stats[i].length; i++)
          totalStats[i][j] += stats[i][j];

    }
    return new PlayerStats(totalStats);
  }
}

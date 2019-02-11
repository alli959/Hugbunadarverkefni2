
package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.entities.Game;
import project.persistence.entities.GameEvent;
import project.persistence.entities.User;
import project.persistence.repositories.*;
import project.controller.Toolkit;

import javax.servlet.http.HttpSession;

@RestController
public class MainController {

  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private GameRepository gameRepository;

  // Unused
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String user(HttpSession session, Model model) {
    User loggedInUser = (User) session.getAttribute("login");
    if(loggedInUser != null){
      model.addAttribute("msg", loggedInUser.getName());
      return "main/Main";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/stats", method = RequestMethod.GET)
  public String teamstats(HttpSession session, Model model) {
    User loggedInUser = (User) session.getAttribute("login");
    if(loggedInUser != null){
      model.addAttribute("msg", loggedInUser.getName());
      // System.out.println(teamRepository.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));
      // ORIGINAL
      // model.addAttribute("teams",teamRepository.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));
      model.addAttribute("teams",teamRepository.findAll());
      return "main/TeamStatView";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/stats/{teamId}", method = RequestMethod.GET)
  public String stats(HttpSession session, Model model, @PathVariable Long teamId) {
    User loggedInUser = (User) session.getAttribute("login");
    if (loggedInUser != null) {

      Iterable<Game> games = gameRepository.findAll();
      for (Game game : games) {

        /*
        model.addAttribute("players", playerStatsRepository.getByTeamId(teamId));
        model.addAttribute("msg", loggedInUser.getName());
        model.addAttribute("players", playerStatsRepository.getByTeamId(teamId));
        */
      }
      return "main/StatView";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logout(HttpSession session, Model model){
    session.removeAttribute("login");
    session.setAttribute("error", "User logged out");

    return "redirect:/login";
  }

  @RequestMapping(value = "/user/pregame", method = RequestMethod.GET)
  public String teamSelect(HttpSession session, Model model){

    User loggedInUser = (User) session.getAttribute("login");
    if(loggedInUser != null) {
      model.addAttribute("msg", loggedInUser.getName());
      model.addAttribute("teams", Toolkit.idsToEntities(loggedInUser.getTeamIds(), teamRepository));
      Iterable<Game> games = gameRepository.findAll();
      for(Game game : games){
        gameRepository.delete(game);
      }
      return "preGame/teamSelect";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/pregame/{teamId}",  method = RequestMethod.GET)
  public String teamGetFromId(@PathVariable Long teamId,
  HttpSession session,
  Model model){

    User loggedInUser = (User)session.getAttribute("login");
    if(loggedInUser != null){

      model.addAttribute("msg", loggedInUser.getName());

      /*
      // Change to startingLineup

      List<Game> bench = gameRepository.getBench();
      List<Game> playing = gameRepository.getPlaying();

      List<Player> initBench = new ArrayList<>();
      List<Player> initPlaying = new ArrayList<>();


      // Ugly for loops to add bench or playing

      for(int i = 0; i<bench.size(); i++){
        initBench.add(playerRepository.findOne(bench.get(i).getPlayerId()));
      }

      for(int i = 0; i<playing.size(); i++){
        initPlaying.add(playerRepository.findOne(playing.get(i).getPlayerId()));
      }
      */

      /*
      // TODO: FIX
      model.addAttribute("players", initBench);
      model.addAttribute("starters", initPlaying);
      model.addAttribute("error", session.getAttribute("error"));
      session.removeAttribute("error");
      session.setAttribute("playing", initPlaying);
      session.setAttribute("bench", initBench);
      session.setAttribute("teamId", teamId);
      */

      return "preGame/startingLineup";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }


  @RequestMapping(value = "/user/pregame/{teamId}/{playerId}",  method = RequestMethod.GET)
  public String playerGetFromId(
    @PathVariable Long teamId,
    @PathVariable Long playerId,
    HttpSession session,
    Model model){
    User loggedInUser = (User)session.getAttribute("login");
    Game game = gameRepository.findById(playerId).get();
    /*
    if(loggedInUser != null) {
      player.setBench(!player.isBench());
      gameRepository.save(player);
      return "redirect:/user/pregame/{teamId}";
    }
    */
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  /*-------HELPER FUNCTIONS DON'T LOOK --------- */

  public int getThrees(int[] gameEventsByLocation) {
    return gameEventsByLocation[GameEvent.RIGHT_WING]
    + gameEventsByLocation[GameEvent.LEFT_WING]
    + gameEventsByLocation[GameEvent.TOP]
    + gameEventsByLocation[GameEvent.RIGHT_CORNER]
    + gameEventsByLocation[GameEvent.LEFT_CORNER];
  }

  public int getTwos(int[] gameEventsByLocation) {
    return gameEventsByLocation[GameEvent.RIGHT_TOP]
    + gameEventsByLocation[GameEvent.LEFT_TOP]
    + gameEventsByLocation[GameEvent.RIGHT_TOP]
    + gameEventsByLocation[GameEvent.LEFT_SHORT]
    + gameEventsByLocation[GameEvent.RIGHT_SHORT]
    + gameEventsByLocation[GameEvent.LAY_UP];
  }

  public int getThreeHit(Long gameId, Long playerId){
    Game game = gameRepository.findById(gameId).get();
    int[][] stats = game.compileStats(playerId);
    return getThrees(stats[GameEvent.HIT]);
  }

  public int getThreeMiss(Long gameId, Long playerId){
    Game game = gameRepository.findById(gameId).get();
    int[][] stats = game.compileStats(playerId);
    return getThrees(stats[GameEvent.MISS]);
  }

  public int getTwoHit(Long gameId, Long playerId){
    Game game = gameRepository.findById(gameId).get();
    int[][] stats = game.compileStats(playerId);
    return getTwos(stats[GameEvent.HIT]);
  }

  public int getTwoMiss(Long gameId, Long playerId){
    Game game = gameRepository.findById(gameId).get();
    int[][] stats = game.compileStats(playerId);
    return getTwos(stats[GameEvent.MISS]);
  }

}

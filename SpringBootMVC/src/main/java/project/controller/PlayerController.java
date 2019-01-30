
package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Player;
import project.persistence.entities.User;
import project.persistence.entities.Team;
import project.persistence.repositories.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
public class PlayerController {

  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private TeamRepository teamRepository;

  @RequestMapping(value = "/user/team/{teamId}/player", method = RequestMethod.GET)
  public String playerAddGet(@PathVariable Long teamId, HttpSession session, Model model){

    User loggedInUser = (User)session.getAttribute("login");
    Team team = teamRepository.findById(teamId).get();
    if(loggedInUser != null) {
      model.addAttribute("msg",loggedInUser.getName());
      model.addAttribute("teamId", teamId);
      model.addAttribute("playerAdd", new Player());
      model.addAttribute("playerNo", team.getPlayers().size());
      // model.addAttribute("playerNo", playerRepository.countPlayersInTeam(teamId).get(0));
      model.addAttribute("players", team.getPlayers());
      return "player/Player";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
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
    User loggedInUser = (User)session.getAttribute("login");
    if(loggedInUser != null) {
      // TODO:
      // Laga thannig ad thetta notar ekki playerstats
      // List<PlayerStats> player = playerStatsRepository.getByPlayerId(playerId);
      model.addAttribute("msg", loggedInUser.getName());
      model.addAttribute("teamId", teamId);
      model.addAttribute("playerId", playerId);
      model.addAttribute("players", playerRepository.findById(playerId));
      return "player/playerView";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }
}

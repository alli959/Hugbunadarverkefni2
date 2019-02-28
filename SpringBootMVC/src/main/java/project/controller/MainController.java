
package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.entities.*;
import project.persistence.repositories.*;
import project.controller.Toolkit;

import java.util.List;

import javax.servlet.http.HttpSession;


@CrossOrigin
@RestController
public class MainController {

  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private GameRepository gameRepository;

  // Method: localhost:8080/user/stats/teamStats?teamId=[teamId]
  // Return: Stats of the team with id = teamId
  @RequestMapping(value="/user/stats/teamStats", method=RequestMethod.GET)
  public Stats teamStats(@RequestParam Long teamId) {
    Team team = teamRepository.findById(teamId).get();
    List<Game> listOfGamesPlayed = Toolkit.idsToEntities(team.getGamesPlayed(), gameRepository);
    Stats stats = new Stats();
    for (Game game : listOfGamesPlayed)
      stats.addData(game.compileStats());
    stats.recalculateStats();
    return stats;
  }
}

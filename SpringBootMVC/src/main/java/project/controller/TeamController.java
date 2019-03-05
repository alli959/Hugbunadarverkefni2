package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.entities.*;
import project.persistence.repositories.*;
import project.controller.Toolkit;

import java.util.List;

@RestController
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private PlayerRepository playerRepository;
  @Autowired
  private UserRepository userRepository;

  // Method: localhost:8080/user/team
  // Return: All teams user has as JSON
  @RequestMapping(value = "/user/team", method = RequestMethod.GET)
  public Iterable<Team> getAllTeams(
    @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);

    User user = userRepository.findById(userName).get();
    return Toolkit.idsToEntities(user.getTeamIds(), teamRepository);
  }

  // Method: localhost:8080/user/saveTeam?name=[string]
  // Return: The newly created team as JSON
  @RequestMapping(value = "/user/saveTeam", method = RequestMethod.GET)
  public Team saveTeam(
    @RequestHeader("Authorization") String basicAuthString,
    @RequestParam String name,
    @RequestParam(required=false) Long id,
    @RequestParam(required=false) List<Long> playerIds,
    @RequestParam(required=false) List<Long> gamesPlayed
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    System.out.println(userName);
    User user = userRepository.findById(userName).get();
    Team team = new Team();
    if (id != null)
      team = teamRepository.findById(id).get();
    team.setName(name);
    team.setUserOwner(userName);
    if (gamesPlayed != null)
      team.setGamesPlayed(gamesPlayed);
    if (playerIds != null)
      for (Long playerId : playerIds)
        team.addPlayer(playerRepository.findById(playerId).get());
    team = teamRepository.save(team);
    user.addTeamId(team.getId());
    userRepository.save(user);
    return team;
  }

  @RequestMapping(value = "/user/team/remove", method = RequestMethod.GET)
  public String removeOneTeam(
      @RequestParam  Long id
      ) {
    teamRepository.deleteById(id);
    return "Success";
      }

  // Method: localhost:8080/user/team/getOne?id=[long]
  // Return: The team with id provided as JSON
  @RequestMapping(value = "/user/team/getOne",  method = RequestMethod.GET)
  public Team getOneTeam(
      @RequestParam Long id
      ){
    return teamRepository.findById(id).get();
      }
}

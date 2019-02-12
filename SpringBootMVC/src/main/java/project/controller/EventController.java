package project.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.controller.Toolkit;
import project.persistence.entities.*;
import project.persistence.repositories.*;
import project.service.*;

@RestController
public class EventController {

  // Instance Variables
  StringManipulationService stringService;

  @Autowired
  GameRepository gameRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  TeamRepository teamRepository;
  @Autowired
  PlayerRepository playerRepository;
  @Autowired
  GameEventRepository gameEventRepository;

  // Method: localhost:8080/user/hasActiveGame
  // Return: "true" if has a game that he's gathering stats for, "false" otherwis
  @RequestMapping(value="/user/hasActiveGame", method=RequestMethod.GET)
  public Boolean hasActiveGame(
      @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    return (user.getCurrentGame() != null);
  }

  // Method: localhost:8080/user/setActiveGame?gameId=[gameId]
  // Return: "Success" if active game has been set
  @RequestMapping(value="/user/setActiveGame", method=RequestMethod.GET)
  public String setActiveGame(
      @RequestHeader("Authorization") String basicAuthString,
      @RequestParam Long gameId
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    Game game = gameRepository.findById(gameId).get();
    user.setCurrentGame(game);
    userRepository.save(user);
    return "Success";
  }

  // Method: localhost:8080/user/getActiveGame
  // Return: JSON with the current game user is gathering stats for
  @RequestMapping(value="/user/getActiveGame", method=RequestMethod.GET)
  public Game getActiveGame(
      @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    return user.getCurrentGame();
  }

  // Method: localhost:8080/user/createGame?bench=[playerId]&...&playing=[playerId]&...&teamId=[teamId]
  // Attntn: ... means that ...&key=value1&key=value2&key=value3... is allowed
  // Notreq: stadiumName, timeOfGame
  // Return: Game newly created
  // After : Team and players of the team have id of game
  @RequestMapping(value="/user/createGame", method=RequestMethod.GET)
  public Game createGame(
      @RequestHeader("Authorization") String basicAuthString,
      @RequestParam ArrayList<Long> bench,
      @RequestParam ArrayList<Long> playing,
      @RequestParam Long teamId,
      @RequestParam(required=false) String stadiumName,
      @RequestParam(required=false) Long timeOfGame
  ) {
    Game game = new Game();
    List<Player> startingLineup = Toolkit.idsToEntities(playing, playerRepository);
    List<Player> theRest = Toolkit.idsToEntities(bench, playerRepository);

    game.setStartingLineup(startingLineup);
    game.setBench(theRest);
    game.setStadiumName(stadiumName);
    game.setTimeOfGame(timeOfGame);
    game.setTeamId(teamId);

    game = gameRepository.save(game);
    for (Player player : game.getAllPlayers()) {
      player.addGamePlayed(game.getId());
      playerRepository.save(player);
    }

    Team team = teamRepository.findById(teamId).get();
    team.addGamePlayed(game.getId());
    teamRepository.save(team);

    return game;
  }

  // Method: localhost:8080/user/addGameEvent?location=[string]&eventType=[string]&time=[number]&playerId=[playerId]
  // Note  :
  //    location can be one of ( "NONE", "LEFT_WING", "RIGHT_WING", "TOP", "LEFT_CORNER", "RIGHT_CORNER", "LEFT_SHORT",
  //                             "RIGHT_SHORT", "LEFT_TOP", "RIGHT_TOP", "LAY_UP", "FREE_THROW")
  //    eventType can be one of ("MISS", "HIT", "FOUL", "ASSIST", "REBOUND", "BLOCK", "TURNOVER")
  // Return: 
  @RequestMapping(value="/user/addGameEvent")
  public GameEvent addGameEvent (
      @RequestHeader("Authorization") String basicAuthString,
      @RequestParam(required=false) String location,
      @RequestParam String eventType,
      @RequestParam Long time,
      @RequestParam(required=false) Long playerId
  ) throws Exception {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    Game currentGame = user.getCurrentGame();
    Long timeOfEvent = currentGame.getTimeOfGame() + time;
    GameEvent gameEvent = new GameEvent();
    int locationValue = location == null ? 0 : GameEvent.getLocationByName(location);

    gameEvent.setLocation(locationValue);
    gameEvent.setEventType(GameEvent.getEventTypeByName(eventType));
    gameEvent.setTimeOfEvent(timeOfEvent);
    if (playerId != null) {
      gameEvent.setPlayerId(playerId);
    }
    gameEvent = gameEventRepository.save(gameEvent);
    currentGame.addGameEvent(gameEvent);
    gameRepository.save(currentGame);
    return gameEvent;
  }

  // Unused
  @RequestMapping(value = "/game", method = RequestMethod.POST)
  public void ShotMade(@RequestBody String shotAction) throws JSONException, Exception {

    // Muna ad skoda thetta herna og skrifa formattid fyrir ofan a strengnum
    System.out.println(shotAction);
    //-------access the json object ---------//
    ////playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other////
    JSONObject myObject = new JSONObject(shotAction);
    System.out.println(myObject.toString());
  }

  // Method: localhost:8080/user/endGame
  // Return: "Success" if the current game of the user has been unset (set to null)
  @RequestMapping(value = "/user/endGame", method = RequestMethod.GET)
  public String endgame(
      @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    user.setCurrentGame(null);
    userRepository.save(user);
    return "Success, game has been removed";
  }
}


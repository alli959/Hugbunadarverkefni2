package project.controller;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.entities.Player;
import project.persistence.entities.User;
import project.service.*;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class EventController {

  // Instance Variables
  StringManipulationService stringService;

  @RequestMapping(value = "/game", method = RequestMethod.GET)
  public String home(HttpSession session, Model model) {
    String action = (String) session.getAttribute("Action");
    User loggedInUser = (User) session.getAttribute("login");

    List<Player> playing = (List<Player>) session.getAttribute("playing");
    List<Player> bench = (List<Player>) session.getAttribute("bench");

    Long teamId = (Long) session.getAttribute("teamId");

    if (loggedInUser != null) {
      if (playing.toArray().length < 5 || playing.toArray().length > 5) {
        session.setAttribute("error", "Starting lineup should be 5 \n not less not more, \n only 5");
        return "redirect:/user/pregame/" + teamId;
      }
      model.addAttribute("starters", playing);
      model.addAttribute("players", bench);
      return "Game";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  // Breyta thessu yfir i method af 3 breytum
  // Location
  // EventType
  // Time
  // PlayerId

  @RequestMapping(value = "/game", method = RequestMethod.POST)
  public void ShotMade(@RequestBody String shotAction) throws JSONException, Exception {

    // Muna ad skoda thetta herna og skrifa formattid fyrir ofan a strengnum
    System.out.println(shotAction);
    //-------access the json object ---------//
    ////playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other////
    JSONObject myObject = new JSONObject(shotAction);

    System.out.println(myObject.toString());
  }

  @RequestMapping(value = "/game/endgame", method = RequestMethod.GET)
  public String endgame(HttpSession session, Model model) {

    return "redirect:/user/stats";
  }

}

/***
//-----Add shots -------//
String playerIdText = myObject.get("playerId").toString();
String from = myObject.get("from").toString();
if(!playerIdText.equals("") && !from.equals("")) {
Long playerId = Long.parseLong(playerIdText);
boolean isHit = (boolean) myObject.get("isHit");
Game shooter = gameRepository.findById(playerId);
if(isHit) {
Long shot = Long.parseLong(shooter.getClass().getMethod("get" + from + "Hit").invoke(shooter).toString());
shooter.getClass().getMethod("set" + from + "Hit", Long.class).invoke(shooter, shot += 1);
}
else{
Long shot = Long.parseLong(shooter.getClass().getMethod("get" + from + "Miss").invoke(shooter).toString());
shooter.getClass().getMethod("set" + from + "Miss", Long.class).invoke(shooter, shot += 1);
}
gameRepository.save(shooter);
}

//-------Add assist------//
String assistIdText = myObject.get("assist").toString();
// System.out.println(assistIdText);
if(!assistIdText.equals("")) {
Long assistId = Long.parseLong(assistIdText);
Game assister = gameRepository.findByPlayerId(assistId);
Long assist = Long.parseLong(assister.getClass().getMethod("getAssist").invoke(assister).toString());
assister.getClass().getMethod("setAssist", Long.class).invoke(assister, assist += 1);
gameRepository.save(assister);
}

//-------Add rebound-------//
String reboundIdText = myObject.get("rebound").toString();
// System.out.println(reboundIdText);
if(!reboundIdText.equals("")) {
Long reboundId = Long.parseLong(reboundIdText);
Game rebounder = gameRepository.findByPlayerId(reboundId);
Long rebound = Long.parseLong(rebounder.getClass().getMethod("getRebound").invoke(rebounder).toString());
rebounder.getClass().getMethod("setRebound", Long.class).invoke(rebounder, rebound += 1);
gameRepository.save(rebounder);
}

////playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other////

//-----------Add steal---------//
String stealText = myObject.get("other").toString();
String stealIdText = myObject.get("playerId").toString();
if(stealText.equals("Steal") && !stealIdText.equals("")){
Long stealId = Long.parseLong(stealIdText);
Game stealer = gameRepository.findByPlayerId(stealId);
Long steal = Long.parseLong(stealer.getClass().getMethod("getSteal").invoke(stealer).toString());
stealer.getClass().getMethod("setSteal", Long.class).invoke(stealer, steal += 1);
gameRepository.save(stealer);
}

//-----------Add block---------//
String blockText = myObject.get("other").toString();
String blockIdText = myObject.get("playerId").toString();
if(blockText.equals("Block") && !blockIdText.equals("")){
Long blockId = Long.parseLong(blockIdText);
Game blocker = gameRepository.findByPlayerId(blockId);
Long block = Long.parseLong(blocker.getClass().getMethod("getBlock").invoke(blocker).toString());
blocker.getClass().getMethod("setBlock", Long.class).invoke(blocker, block += 1);
gameRepository.save(blocker);
}


//----------Add turnover---------//
String trunoverText = myObject.get("turnover").toString();
String turnoverIdText = myObject.get("playerId").toString();
if(trunoverText.equals("Turnover") && !turnoverIdText.equals("")){
Long turnoverId = Long.parseLong(turnoverIdText);
Game turnoverer = gameRepository.findByPlayerId(turnoverId);
Long turnover = Long.parseLong(turnoverer.getClass().getMethod("getTurnover").invoke(turnoverer).toString());
turnoverer.getClass().getMethod("setTurnover", Long.class).invoke(turnoverer, turnover += 1);
gameRepository.save(turnoverer);
}


//-----------Add foul--------//
String foulText = myObject.get("other").toString();
String foulIdText = myObject.get("playerId").toString();
if(foulText.equals("Foul") && !turnoverIdText.equals("")){
Long foulId = Long.parseLong(foulIdText);
Game fouler = gameRepository.findByPlayerId(foulId);
Long foul = Long.parseLong(fouler.getClass().getMethod("getFoul").invoke(fouler).toString());
fouler.getClass().getMethod("setFoul", Long.class).invoke(fouler, foul += 1);
gameRepository.save(fouler);
}

**/
//----------Add substitutions --------//

/* String subInText = myObject.get("subIn").toString();
String subOutText = myObject.get("subOut").toString();
if(!subInText.equals("") && !subOutText.equals("")){
System.out.println("subOutText " +subOutText);
System.out.println("subInText " +subInText);


Long subInId = Long.parseLong(subInText);
Long subOutId = Long.parseLong(subOutText);



Game subIner = gameRepository.findByPlayerId(subInId);
Game subOuter = gameRepository.findByPlayerId(subOutId);
subIner.setBench(false);
subOuter.setBench(true);
gameRepository.save(subOuter);
gameRepository.save(subIner);
}
*/

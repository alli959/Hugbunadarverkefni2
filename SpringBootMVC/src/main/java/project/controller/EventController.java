package project.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.entities.Game;
import project.persistence.entities.Player;
import project.persistence.entities.PlayerStats;
import project.persistence.entities.Users;
import project.service.*;


import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EventController {

    // Instance Variables
    StringManipulationService stringService;
    private UserService userService;
    private TeamService teamService;
    private PlayerService playerService;
    private GameService gameService;
    private PlayerStatsService playerStatsService;

    // Dependency Injection
    @Autowired
    public EventController(StringManipulationService stringService, UserService userService, TeamService teamService, PlayerService playerService, GameService gameService, PlayerStatsService playerStatsService) {
        this.stringService = stringService;
        this.userService = userService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.playerStatsService = playerStatsService;
    }


    //
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {

        String action = (String) session.getAttribute("Action");
        Users loggedInUser = (Users) session.getAttribute("login");

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


    @RequestMapping(value = "/game", method = RequestMethod.POST)
    public void ShotMade(@RequestBody String shotAction) throws JSONException, Exception {


        //-------access the json object ---------//
        ////playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other////
        JSONObject myObject = new JSONObject(shotAction);

        System.out.println(myObject.toString());

        //-----Add shots -------//
        String playerIdText = myObject.get("playerId").toString();
        String from = myObject.get("from").toString();
        if(!playerIdText.equals("") && !from.equals("")) {
            Long playerId = Long.parseLong(playerIdText);
            boolean isHit = (boolean) myObject.get("isHit");
            Game shooter = gameService.findByPlayerId(playerId);
            if(isHit) {
                Long shot = Long.parseLong(shooter.getClass().getMethod("get" + from + "Hit").invoke(shooter).toString());
                shooter.getClass().getMethod("set" + from + "Hit", Long.class).invoke(shooter, shot += 1);
            }
            else{
                Long shot = Long.parseLong(shooter.getClass().getMethod("get" + from + "Miss").invoke(shooter).toString());
                shooter.getClass().getMethod("set" + from + "Miss", Long.class).invoke(shooter, shot += 1);
            }
            gameService.save(shooter);
        }




        //-------Add assist------//
        String assistIdText = myObject.get("assist").toString();
        // System.out.println(assistIdText);
        if(!assistIdText.equals("")) {
            Long assistId = Long.parseLong(assistIdText);
            Game assister = gameService.findByPlayerId(assistId);
            Long assist = Long.parseLong(assister.getClass().getMethod("getAssist").invoke(assister).toString());
            assister.getClass().getMethod("setAssist", Long.class).invoke(assister, assist += 1);
            gameService.save(assister);
        }

        //-------Add rebound-------//
        String reboundIdText = myObject.get("rebound").toString();
        // System.out.println(reboundIdText);
        if(!reboundIdText.equals("")) {
            Long reboundId = Long.parseLong(reboundIdText);
            Game rebounder = gameService.findByPlayerId(reboundId);
            Long rebound = Long.parseLong(rebounder.getClass().getMethod("getRebound").invoke(rebounder).toString());
            rebounder.getClass().getMethod("setRebound", Long.class).invoke(rebounder, rebound += 1);
            gameService.save(rebounder);
        }

        ////playerId, from, isHit, assist, rebound, subIn, subOut, turnover, other////

        //-----------Add steal---------//
        String stealText = myObject.get("other").toString();
        String stealIdText = myObject.get("playerId").toString();
        if(stealText.equals("Steal") && !stealIdText.equals("")){
            Long stealId = Long.parseLong(stealIdText);
            Game stealer = gameService.findByPlayerId(stealId);
            Long steal = Long.parseLong(stealer.getClass().getMethod("getSteal").invoke(stealer).toString());
            stealer.getClass().getMethod("setSteal", Long.class).invoke(stealer, steal += 1);
            gameService.save(stealer);
        }

        //-----------Add block---------//
        String blockText = myObject.get("other").toString();
        String blockIdText = myObject.get("playerId").toString();
        if(blockText.equals("Block") && !blockIdText.equals("")){
            Long blockId = Long.parseLong(blockIdText);
            Game blocker = gameService.findByPlayerId(blockId);
            Long block = Long.parseLong(blocker.getClass().getMethod("getBlock").invoke(blocker).toString());
            blocker.getClass().getMethod("setBlock", Long.class).invoke(blocker, block += 1);
            gameService.save(blocker);
        }


        //----------Add turnover---------//
        String trunoverText = myObject.get("turnover").toString();
        String turnoverIdText = myObject.get("playerId").toString();
        if(trunoverText.equals("Turnover") && !turnoverIdText.equals("")){
            Long turnoverId = Long.parseLong(turnoverIdText);
            Game turnoverer = gameService.findByPlayerId(turnoverId);
            Long turnover = Long.parseLong(turnoverer.getClass().getMethod("getTurnover").invoke(turnoverer).toString());
            turnoverer.getClass().getMethod("setTurnover", Long.class).invoke(turnoverer, turnover += 1);
            gameService.save(turnoverer);
        }


        //-----------Add foul--------//
        String foulText = myObject.get("other").toString();
        String foulIdText = myObject.get("playerId").toString();
        if(foulText.equals("Foul") && !turnoverIdText.equals("")){
            Long foulId = Long.parseLong(foulIdText);
            Game fouler = gameService.findByPlayerId(foulId);
            Long foul = Long.parseLong(fouler.getClass().getMethod("getFoul").invoke(fouler).toString());
            fouler.getClass().getMethod("setFoul", Long.class).invoke(fouler, foul += 1);
            gameService.save(fouler);
        }

        //----------Add substitutions --------//

       /* String subInText = myObject.get("subIn").toString();
        String subOutText = myObject.get("subOut").toString();
        if(!subInText.equals("") && !subOutText.equals("")){
            System.out.println("subOutText " +subOutText);
            System.out.println("subInText " +subInText);


            Long subInId = Long.parseLong(subInText);
            Long subOutId = Long.parseLong(subOutText);



            Game subIner = gameService.findByPlayerId(subInId);
            Game subOuter = gameService.findByPlayerId(subOutId);
            subIner.setBench(false);
            subOuter.setBench(true);
            gameService.save(subOuter);
            gameService.save(subIner);
        }*/






    }

    @RequestMapping(value = "/game/endgame", method = RequestMethod.GET)
    public String endgame(HttpSession session, Model model) {

        return "redirect:/user/stats";
    }







}

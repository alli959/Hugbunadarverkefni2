package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Player;
import project.persistence.entities.Users;
import project.persistence.entities.PlayerStats;
import project.service.PlayerStatsService;
import project.service.PlayerService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class PlayerController {

    private PlayerService playerService;

    private PlayerStatsService playerStatsService;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerStatsService playerStatsService){
        this.playerService = playerService;
        this.playerStatsService = playerStatsService;
    };




    @RequestMapping(value = "/user/team/{teamId}/player", method = RequestMethod.GET)
    public String playerAddGet(@PathVariable Long teamId, HttpSession session, Model model){

        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null) {

            model.addAttribute("msg",loggedInUser.getName());
            model.addAttribute("teamId", teamId);

            model.addAttribute("playerAdd", new Player());


            model.addAttribute("playerNo", playerService.countPlayersInTeam(teamId).get(0));


            model.addAttribute("players", playerService.findPlayersInTeamReverseOrder(teamId));

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

        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null) {
            playerService.save(player);
            model.addAttribute("msg", loggedInUser.getName());

            model.addAttribute("teamId", teamId);


            model.addAttribute("playerNo", playerService.countPlayersInTeam(teamId).get(0));
            model.addAttribute("players", playerService.findPlayersInTeamReverseOrder(teamId));


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


        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null) {
            List<PlayerStats> player = playerStatsService.getByPlayerId(playerId);

            model.addAttribute("msg", loggedInUser.getName());
            model.addAttribute("teamId", teamId);

            model.addAttribute("playerId", playerId);

            model.addAttribute("players", player);


            return "player/playerView";
        }
        session.setAttribute("error", "User must be logged in!");

        return "redirect:/login";
    }



}







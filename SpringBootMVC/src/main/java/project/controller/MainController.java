package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Game;
import project.persistence.entities.PlayerStats;
import project.persistence.entities.Users;
import project.service.GameService;
import project.service.UserService;
import project.service.PlayerStatsService;
import project.service.TeamService;
import project.persistence.entities.Player;
import project.service.PlayerService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller

public class MainController {
    private UserService userService;
    private TeamService teamService;
    private PlayerService playerService;
    private GameService gameService;
    private PlayerStatsService playerStatsService;


    @Autowired
    public MainController(UserService userService, TeamService teamService, PlayerService playerService, GameService gameService, PlayerStatsService playerStatsService){
        this.userService = userService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.gameService = gameService;
        this.playerStatsService = playerStatsService;
    }


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(HttpSession session, Model model) {
        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null){
            model.addAttribute("msg", loggedInUser.getName());
            return "main/Main";
        }
        session.setAttribute("error", "User must be logged in!");

        return "redirect:/login";

    }

    @RequestMapping(value = "/user/stats", method = RequestMethod.GET)
    public String teamstats(HttpSession session, Model model) {
        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null){

            model.addAttribute("msg", loggedInUser.getName());

            // System.out.println(teamService.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));
            model.addAttribute("teams",teamService.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));

            return "main/TeamStatView";
        }
        session.setAttribute("error", "User must be logged in!");

        return "redirect:/login";

    }

    @RequestMapping(value = "/user/stats/{teamId}", method = RequestMethod.GET)
    public String stats(HttpSession session, Model model, @PathVariable Long teamId) {
        Users loggedInUser = (Users) session.getAttribute("login");
        if (loggedInUser != null) {
            List<Game> players = gameService.findAllReverseOrder();


            for (int i = 0; i < players.size(); i++) {
                Long threeHit = getThreeHit(players.get(i).getPlayerId());
                Long threeMiss = getThreeMiss(players.get(i).getPlayerId());
                Long totalThreeAttemts = threeHit + threeMiss;
                Long twoHit = getTwoHit(players.get(i).getPlayerId());
                Long twoMiss = getTwoMiss(players.get(i).getPlayerId());
                Long freeThrowHit = players.get(i).getFreeThrowHit();
                Long freeThrowMiss = players.get(i).getFreeThrowMiss();
                Long totalTwoAttemts = twoHit + twoMiss;
                Long totalFreeThrowAttemts = freeThrowHit + freeThrowMiss;


                double threeHitPercentF = 0.00;
                double twoHitPercentF = 0.00;
                double freeThrowHitPercentF = 0.00;

                if (threeHit != 0) {
                    threeHitPercentF = ((double) threeHit / (double) totalThreeAttemts) * 100;
                }
                if (twoHit != 0) {
                    twoHitPercentF = ((double) twoHit / (double) totalTwoAttemts) * 100;
                }



                if(freeThrowHit != 0){
                    freeThrowHitPercentF = ((double) freeThrowHit / (double) totalFreeThrowAttemts) * 100;
                }



                // System.out.println("threeHitPercentF " + threeHitPercentF);
                // System.out.println("twoHitPercentF " + twoHitPercentF);
                // System.out.println("threeHit " + threeHit);
                // System.out.println("threeMiss " + threeMiss);
                // System.out.println("twoHit " + twoHit);
                // System.out.println("twoMiss " + twoMiss);
                // System.out.println("totalTwoAttemts " + totalTwoAttemts);


                String threeHitPercent = String.valueOf(threeHitPercentF) + '%';
                String twoHitPercent = String.valueOf(twoHitPercentF) + '%';
                String freeThrowHitPercent = String.valueOf(freeThrowHitPercentF) + '%';


                Long playerId = players.get(i).getPlayerId();
                Long turnover = players.get(i).getTurnover();
                Long block = players.get(i).getBlock();
                Long steal = players.get(i).getSteal();
                Long foul = players.get(i).getFoul();
                Long assist = players.get(i).getAssist();
                Long rebound = players.get(i).getRebound();
                Long playerPoints = freeThrowHit + (threeHit * (Long.parseLong(String.valueOf(3)))) + (twoHit * (Long.parseLong(String.valueOf(2))));
                PlayerStats player = new PlayerStats();


                if(playerStatsService.getEntityByPlayerId(playerId) != null) {
                    // System.out.println("hello2");
                    player = playerStatsService.getEntityByPlayerId(playerId);
                }


                player.setPlayerName(playerService.findOne(playerId).getName());
                player.setSteals(player.getSteals() + steal);
                player.setPoints(player.getPoints() + playerPoints);
                player.setTeamId(teamId);
                player.setThreePointHit(player.getThreePointHit() + threeHit);
                player.setThreePointMiss(player.getThreePointMiss() + threeMiss);
                player.setTwoPointHit(player.getTwoPointHit() + twoHit);
                player.setTwoPointMiss(player.getTwoPointMiss() + twoMiss);
                player.setThreePointer(threeHitPercent);
                player.setTwoPointer(twoHitPercent);
                player.setFreeThrowHit(player.getFreeThrowHit() + freeThrowHit);
                player.setFreeThrowMiss(player.getThreePointMiss() + freeThrowMiss);
                player.setFreeThrow(freeThrowHitPercent);
                player.setPlayerId(playerId);
                player.setTurnovers(player.getTurnovers() + turnover);
                player.setBlocks(player.getBlocks() + block);
                player.setFouls(player.getFouls() + foul);
                player.setAssists(player.getAssists() + assist);
                player.setRebounds(player.getRebounds() + rebound);

                //check if player already exists

                List<PlayerStats> playerTest = playerStatsService.getByPlayerId(playerId);
                if (playerTest.size() != 0) {
                    // System.out.println("hello");
                    player.setId(playerTest.get(0).getId());
                }

                playerStatsService.save(player);


                model.addAttribute("players", playerStatsService.getByTeamId(teamId));

                model.addAttribute("msg", loggedInUser.getName());

                model.addAttribute("players", playerStatsService.getByTeamId(teamId));


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

        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null) {
            model.addAttribute("msg", loggedInUser.getName());

            model.addAttribute("teams",teamService.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));

            List<Game> game = gameService.findAllReverseOrder();

            if(game.toArray().length != 0){
                for(int i = 0; i<game.size(); i++){
                    Game aGame = game.get(i);
                    gameService.delete(aGame);
                }
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


        List<Player> players = playerService.findPlayersInTeamReverseOrder(teamId);
        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null){

            model.addAttribute("msg", loggedInUser.getName());

            if(gameService.findAllReverseOrder().toArray().length <= 0){
                for(int i = 0; i<players.size(); i++) {
                    Game player = new Game();
                    player.setBench(true);
                    player.setPlayerId(players.get(i).getId());
                    gameService.save(player);
                }

            }




            List<Game> bench = gameService.getBench();
            List<Game> playing = gameService.getPlaying();

            List<Player> initBench = new ArrayList<>();
            List<Player> initPlaying = new ArrayList<>();


            // Ugly for loops to add bench or playing

            for(int i = 0; i<bench.size(); i++){
                initBench.add(playerService.findOne(bench.get(i).getPlayerId()));
            }

            for(int i = 0; i<playing.size(); i++){
                initPlaying.add(playerService.findOne(playing.get(i).getPlayerId()));
            }

            model.addAttribute("players",initBench);
            model.addAttribute("starters",initPlaying);
            model.addAttribute("error",session.getAttribute("error"));
            session.removeAttribute("error");
            session.setAttribute("playing",initPlaying);
            session.setAttribute("bench",initBench);
            session.setAttribute("teamId", teamId);

            return "preGame/startingLineup";


        }
        session.setAttribute("error", "User must be logged in!");

        return "redirect:/login";
    }


    @RequestMapping(value = "/user/pregame/{teamId}/{playerId}",  method = RequestMethod.GET)
    public String playerGetFromId(@PathVariable Long teamId,
                                  @PathVariable Long playerId,
                                  HttpSession session,
                                  Model model){


        Users loggedInUser = (Users)session.getAttribute("login");
        Game player = gameService.findByPlayerId(playerId);
        if(loggedInUser != null) {

            player.setBench(!player.isBench());
            gameService.save(player);



            return "redirect:/user/pregame/{teamId}";
        }

        session.setAttribute("error", "User must be logged in!");


        return "redirect:/login";

    }


    /*-------HELPER FUNCTIONS DON'T LOOK --------- */

    public Long getThreeHit(Long playerId){
        Game game = gameService.findByPlayerId(playerId);
        long value = 0;
        value += game.getLeftWingThreeHit();
        value += game.getRightWingThreeHit();
        value += game.getTopThreeHit();
        value += game.getLeftCornerThreeHit();
        value += game.getRightCornerThreeHit();
        return value;

    }



    public Long getThreeMiss(Long playerId){
        Game game = gameService.findByPlayerId(playerId);
        long value = 0;
        value += game.getLeftWingThreeMiss();
        value += game.getRightWingThreeMiss();
        value += game.getTopThreeMiss();
        value += game.getLeftCornerThreeMiss();
        value += game.getRightCornerThreeMiss();
        return value;

    }

    public Long getTwoHit(Long playerId){
        Game game = gameService.findByPlayerId(playerId);
        long value = 0;
        value += game.getLeftShortCornerHit();
        value += game.getRightShortCornerHit();
        value += game.getLeftTopKeyHit();
        value += game.getRightTopKeyHit();
        value += game.getTopKeyHit();
        value += game.getLayUpHit();

        return value;
    }

    public Long getTwoMiss(Long playerId){
        Game game = gameService.findByPlayerId(playerId);
        long value = 0;
        value += game.getLeftShortCornerMiss();
        value += game.getRightShortCornerMiss();
        value += game.getLeftTopKeyMiss();
        value += game.getRightTopKeyMiss();
        value += game.getTopKeyMiss();
        value += game.getLayUpMiss();

        return value;
    }









}

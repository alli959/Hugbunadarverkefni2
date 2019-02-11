
package project.persistence.entities;


import javax.persistence.*;
import java.util.ArrayList;
import project.persistence.entities.Game;

@Entity
@Table(name="user2")
public class User {
  
  @Id
  private String userName;
  private String name;
  private String password;
  private String email;
  @OneToOne
  private Game currentGame;

  private ArrayList<Long> gameIds = new ArrayList<>();
  private ArrayList<Long> playerIds = new ArrayList<>();
  private ArrayList<Long> teamIds = new ArrayList<>();

  //private Team team;


  public User() {
  }

  public User(String name, String userName, String password, String email) {
    this.name = name;
    this.userName = userName;
    this.password = password;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Game getCurrentGame() {
    return currentGame;
  }

  public void setCurrentGame(Game currentGame) { 
    this.currentGame = currentGame;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ArrayList<Long> getGameIds() {
    return gameIds;
  }

  public void setGameIds(ArrayList<Long> gameIds) {
    this.gameIds = gameIds;
  }

  public ArrayList<Long> getPlayerIds() {
    return playerIds;
  }

  public void setPlayerIds(ArrayList<Long> playerIds) {
    this.playerIds = playerIds;
  }

  public ArrayList<Long> getTeamIds() {
    return teamIds;
  }

  public void setTeamIds(ArrayList<Long> teamIds) {
    this.teamIds = teamIds;
  }

  public void addTeamId(Long teamId) {
    this.teamIds.add(teamId);
  }
}

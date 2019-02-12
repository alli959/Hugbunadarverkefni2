

  ## Method: localhost:8080/user/whatismyusername
     Return: Username of the user


  ## Method: localhost:8080/user/hasActiveGame
     Return: "true" if has a game that he's gathering stats for, "false" otherwis


  ## Method: localhost:8080/user/setActiveGame?gameId=[gameId]
     Return: "Success" if active game has been set


  ## Method: localhost:8080/user/getActiveGame
     Return: JSON with the current game user is gathering stats for


  ## Method: localhost:8080/user/createGame?bench=[playerId]&...&playing=[playerId]&...&teamId=[teamId]
     Attntn: ... means that ...&key=value1&key=value2&key=value3... is allowed
     Notreq: stadiumName, timeOfGame
     Return: Game newly created
     After : Team and players of the team have id of game


  ## Method: localhost:8080/user/addGameEvent?location=[string]&eventType=[string]&time=[number]&playerId=[playerId]
     Note  :
        location can be one of ( "NONE", "LEFT_WING", "RIGHT_WING", "TOP", "LEFT_CORNER", "RIGHT_CORNER", "LEFT_SHORT",
                                 "RIGHT_SHORT", "LEFT_TOP", "RIGHT_TOP", "LAY_UP", "FREE_THROW")
        eventType can be one of ("MISS", "HIT", "FOUL", "ASSIST", "REBOUND", "BLOCK", "TURNOVER")
     Return: 


  ## Method: localhost:8080/user/endGame
     Return: "Success" if the current game of the user has been unset (set to null)


  ## Method: localhost:8080/register?userName=[string]&password=[string]&name=[string]&email=[string]
     Attntn: Needs the header 'Authorization' to be set to 
        Username: 'anonymous'
        Password: 'anonymous'
     Return: userName of the user created if successful, if already exists then "User already exists bruh"


  ## Method: localhost:8080/user/stats/teamStats?teamId=[teamId]
     Return: Stats of the team with id = teamId


  ## Method: localhost:8080/user/team
     Return: All teams user has as JSON


  ## Method: localhost:8080/user/createTeam?name=[string]
     Return: The newly created team as JSON


  ## Method: localhost:8080/user/team/getOne?teamId=[long]
     Return: The team with teamId provided as JSON


  ## Method: localhost:8080/user/createPlayer?playerNr[int]&teamId=[long]&name=[string]&playerPos=[string]
     Return: The player created as JSON


  ## Method: localhost:8080/user/getPlayer?playerId=[long]
     Return: Player with id as playerId as JSON


  ## Method: localhost:8080/user/getStatsForPlayer?playerId=[long]
     Return: Stats object of the player with playerId as JSON

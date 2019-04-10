package yolo.basket;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import yolo.basket.db.Database;
import yolo.basket.db.game.Game;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;



// TODO:
// Endgame button
// Add time to the the stuff
// Undo

public class GameActivity extends AppCompatActivity {

    // Game
    private EndGameTask endGameTask;
    private AddGameEventTask addGameEventTask;
    private int action;
    private int location;
    private long timeOfEvent;
    private Game game;
    private int scoreHome = 0;
    private TextView homeScore;


    // Timer
    private static final long TIME = 600000;
    private TextView klukka;
    private Button startPauseTime;
    private Button setTime;
    private CountDownTimer mCountDownTimer;
    private boolean timerRunning;
    String timeText;
    private Long timeLeft = TIME;

    //Away team
    private Button madeShotAway;
    private Button missedShotAway;
    private Button foulAway;
    private Button turnoverAway;
    private TextView awayScore;
    private int scoreAway = 0;


    //Player
    private static Player selectedPlayer;

    private String[] HomeplayersArray = {
            "LeBron James",
            "Kyle Kuzma",
            "Lonzo Ball",
            "JaVale McGee",
            "Brandon Ingram"
    };

    private List<Player> homePlayers = new ArrayList<>();
    private ArrayList<Player> awayPlayers = new ArrayList<>();

    private ArrayList<String> HomeplayersArr = new ArrayList<>(Arrays.asList(HomeplayersArray));
    private ArrayList<Button> homePlayerButtons = new ArrayList<>();

    private String[] AwayplayersArray = {
            "Stephen Curry",
            "Klay Thompson",
            "Kevin Durant",
            "Draymond Greeen",
            "DeMarcus Cousins"
    };


    public void defineButtons(){
        findViewById(R.id.StartPauseTimer).setOnClickListener(startPauseTimerListener);
        findViewById(R.id.SetTimer).setOnClickListener(setTimerListener);
        findViewById(R.id.madeShotAway).setOnClickListener(awayMadeShotListener);
        findViewById(R.id.foulAway).setOnClickListener(awayFoulListener);
        findViewById(R.id.missedShotAway).setOnClickListener(awayMissedShotListener);
        findViewById(R.id.turnoverAway).setOnClickListener(awayTurnoverListener);
    }

    private Player getSelectedPlayer() {
       return selectedPlayer;
    }

    GameActivity g;

    private ImageView court;

    private double[][] LOCATIONS = {
          // x, y, location
            {0.09, 0.18, GameEvent.LEFT_CORNER  },
            {0.90, 0.18, GameEvent.RIGHT_CORNER },
            {0.86, 0.59, GameEvent.RIGHT_WING   },
            {0.66, 0.64, GameEvent.LEFT_WING    },
            {0.74, 0.16, GameEvent.RIGHT_SHORT  },
            {0.74, 0.27, GameEvent.RIGHT_SHORT  },
            {0.26, 0.16, GameEvent.LEFT_SHORT   },
            {0.26, 0.27, GameEvent.LEFT_SHORT   },
            {0.50, 0.29, GameEvent.LAY_UP       },
            {0.50, 0.50, GameEvent.TOP          },
            {0.31, 0.79, GameEvent.RIGHT_TOP    },
            {0.66, 0.80, GameEvent.LEFT_TOP     }
    };

    private int getLocation(double ratioX, double ratioY) {
        int loc = 0;
        double minDistance = 999999;
        for (double[] location : LOCATIONS) {
            double distXsquared = Math.pow(ratioX - location[0], 2);
            double distYsquared = Math.pow(ratioY - location[1], 2);
            if (minDistance > distXsquared + distYsquared) {
                minDistance = distXsquared + distYsquared;
                // add a little bit for the float -> int conversion
                loc = (int) (location[2] + 0.0001);
            }
        }
        return loc;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        g = this;

        court = findViewById(R.id.basketBallCourt);
        court.setOnTouchListener((view, event) -> {
            double x = event.getX();
            double y = event.getY();
            double height = view.getHeight();
            double width = view.getWidth();
            location = getLocation(x / width, y / height);
            createShotAlert();
            return false;
        });

        startPauseTime = (Button) findViewById(R.id.StartPauseTimer);
        setTime = (Button) findViewById(R.id.SetTimer);

        klukka = (TextView) findViewById(R.id.Timer);

        homeScore = (TextView) findViewById(R.id.homeScore);
        awayScore = (TextView) findViewById(R.id.awayScore);

        defineButtons();
        upDateTimer();
        loadGame();
    }

    private void createShotAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        builder.setTitle("Action for " + playerName);

        builder.setItems(new CharSequence[]{"Made Shot", "Missed Shot", "Committed Foul", "Turnover"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            action = GameEvent.HIT;
                            addScoreToHome(2);
                            addGameEvent();
                            createAssistAlert();
                        break;
                        case 1:
                            action = GameEvent.MISS;
                            addGameEvent();
                            createReboundAlert();
                        break;
                        case 2:
                            createFoulAlert(true);
                        break;
                        case 3: action = GameEvent.TURNOVER;
                            addGameEvent();
                            break;
                    }
                });
        builder.create().show();
    }

    private void createFoulAlert(boolean home) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        if(home){
            builder.setTitle(playerName + " fouled a player, choose foul type");
        } else {
            builder.setTitle("Away team committed a foul");
        }

        builder.setItems(new CharSequence[]{"Defensive foul", "Offensive foul(Turnover)"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            createShootingFoulAlert(home);
                            break;
                        case 1:
                            if(home) {
                                action = GameEvent.TURNOVER;
                                addGameEvent();
                            }
                            break;
                    }
                });
        builder.create().show();
    }

    private void createShootingFoulAlert(boolean home) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        if(home){
            builder.setTitle(playerName + " fouled a player, ");
        } else {
            builder.setTitle("Away team committed a foul");
        }

        builder.setItems(new CharSequence[]{"Shooting foul", "Non shooting foul"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if(home){
                                createShootingFoulShotAlert(home, selectedPlayer);
                            } else {
                                createShowPlayers(home);
                            }
                            break;
                        case 1:
                            if(home){
                                action = GameEvent.FOUL;
                                addGameEvent();
                            }
                            break;
                    }
                });
        builder.create().show();
    }

    private void createShowPlayers(boolean home){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Shooting player");
        ArrayList<Player> arrayPlayer =new ArrayList<Player>();
        ArrayList<String> arrayPlayerNames =new ArrayList<String>();
        for (Player player : homePlayers){
                arrayPlayer.add(player);
                arrayPlayerNames.add(player.getName());
        }

        CharSequence[] charPlayers = arrayPlayerNames.toArray(new CharSequence[arrayPlayerNames.size()]);
        Player temp = selectedPlayer;
        builder.setItems(new CharSequence[]{ charPlayers[0], charPlayers[1], charPlayers[2], charPlayers[3], charPlayers[4]},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            selectedPlayer = arrayPlayer.get(0);
                            createShootingFoulShotAlert(home, temp);
                            break;
                        case 1:
                            selectedPlayer = arrayPlayer.get(1);
                            createShootingFoulShotAlert(home, temp);
                            break;
                        case 2:
                            selectedPlayer = arrayPlayer.get(2);
                            createShootingFoulShotAlert(home, temp);
                            break;
                        case 3:
                            selectedPlayer = arrayPlayer.get(3);
                            createShootingFoulShotAlert(home, temp);
                            break;
                        case 4:
                            selectedPlayer = arrayPlayer.get(4);
                            createShootingFoulShotAlert(home, temp);
                            break;
                    }
                });
        builder.create().show();
    }

    private void createShootingFoulShotAlert(boolean home, Player temp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        //String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        builder.setTitle("Shooting foul");

        builder.setItems(new CharSequence[]{"1 Shot", "2 shots", "3 Shots", "Basket made and 1 shot"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            freeThrows(1, home);
                            break;
                        case 1:
                            freeThrows(2, home);
                            break;
                        case 2:
                            freeThrows(3, home);
                            break;
                        case 3:
                            freeThrows(1, home);
                            if(home){
                                addScoreToAway(2);
                            } else {
                                addScoreToHome(2);

                            }
                            break;
                    }
                });
        builder.create().show();
        selectedPlayer = temp;
    }

    private void freeThrows(int amount, boolean home){
        while(amount-- != 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Free throw");

            builder.setItems(new CharSequence[]{"Made", "Missed"},
                    (dialog, which) -> {
                        switch (which) {
                            case 0:
                                if(home){
                                    addScoreToAway(1);
                                } else {
                                    //action = GameEvent.FREE_THROW; virkar ekki!
                                    action = GameEvent.HIT;
                                    location = GameEvent.FREE_THROW;
                                    addGameEvent();
                                    addScoreToHome(1);
                                }
                                break;
                            case 1:
                                if(!home){
                                    action = GameEvent.MISS; //Ætti að vera .Freethrow miss
                                    addGameEvent();
                                }
                                break;
                        }
                    });
            builder.create().show();
        }
    }

    private void createReboundAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        builder.setTitle(playerName + " missed the shot, rebound for");
        ArrayList<Player> arrayPlayer =new ArrayList<Player>();
        ArrayList<String> arrayPlayerNames =new ArrayList<String>();
        for (Player player : homePlayers){
                arrayPlayer.add(player);
                arrayPlayerNames.add(player.getName());
        }

        CharSequence[] charPlayers = arrayPlayerNames.toArray(new CharSequence[arrayPlayerNames.size()]);
        Player temp = selectedPlayer;
        builder.setItems(new CharSequence[]{ charPlayers[0], charPlayers[1], charPlayers[2], charPlayers[3], charPlayers[4], "Away team"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            action = GameEvent.REBOUND;
                            selectedPlayer = arrayPlayer.get(0);
                            addGameEvent();
                            break;
                        case 1:
                            action = GameEvent.REBOUND;
                            selectedPlayer = arrayPlayer.get(1);
                            addGameEvent();
                            break;
                        case 2:
                            action = GameEvent.REBOUND;
                            selectedPlayer = arrayPlayer.get(2);
                            addGameEvent();
                            break;
                        case 3:
                            action = GameEvent.REBOUND;
                            selectedPlayer = arrayPlayer.get(3);
                            addGameEvent();
                            break;
                        case 4:
                            action = GameEvent.REBOUND;
                            selectedPlayer = arrayPlayer.get(4);
                            addGameEvent();
                            break;
                        case 5:
                            action = GameEvent.REBOUND;
                            selectedPlayer = null;
                            break;
                    }
                });
        builder.create().show();
        selectedPlayer = temp;
    }

    private void createBlockAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Shot blocked by");
        ArrayList<Player> arrayPlayer =new ArrayList<Player>();
        ArrayList<String> arrayPlayerNames =new ArrayList<String>();
        for (Player player : homePlayers){
                arrayPlayer.add(player);
                arrayPlayerNames.add(player.getName());
        }

        CharSequence[] charPlayers = arrayPlayerNames.toArray(new CharSequence[arrayPlayerNames.size()]);

        Player temp = selectedPlayer;
        builder.setItems(new CharSequence[]{ charPlayers[0], charPlayers[1], charPlayers[2], charPlayers[3], charPlayers[4] },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            selectedPlayer = arrayPlayer.get(0);
                            action = GameEvent.BLOCK;
                            addGameEvent();
                            break;
                        case 1:
                            selectedPlayer = arrayPlayer.get(1);
                            action = GameEvent.BLOCK;
                            addGameEvent();
                            break;
                        case 2:
                            selectedPlayer = arrayPlayer.get(2);
                            action = GameEvent.BLOCK;
                            addGameEvent();
                            break;
                        case 3:
                            selectedPlayer = arrayPlayer.get(3);
                            action = GameEvent.BLOCK;
                            addGameEvent();
                            break;
                        case 4:
                            selectedPlayer = arrayPlayer.get(4);
                            action = GameEvent.BLOCK;
                            addGameEvent();
                            break;
                    }
                });
        builder.create().show();
        selectedPlayer = temp;
    }

    /**
     * TODO: GameEvent.Steal
     */

    private void createStealAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Steal by");
        ArrayList<Player> arrayPlayer =new ArrayList<Player>();
        ArrayList<String> arrayPlayerNames =new ArrayList<String>();
        for (Player player : homePlayers){
            arrayPlayer.add(player);
            arrayPlayerNames.add(player.getName());
        }

        CharSequence[] charPlayers = arrayPlayerNames.toArray(new CharSequence[arrayPlayerNames.size()]);

        Player temp = selectedPlayer;
        builder.setItems(new CharSequence[]{ charPlayers[0], charPlayers[1], charPlayers[2], charPlayers[3], charPlayers[4], "Pass out of bounds" },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            selectedPlayer = arrayPlayer.get(0);
                            //action = GameEvent.STEAL;
                            //addGameEvent();
                            break;
                        case 1:
                            selectedPlayer = arrayPlayer.get(1);
                            //action = GameEvent.STEAL;
                            //addGameEvent();
                            break;
                        case 2:
                            selectedPlayer = arrayPlayer.get(2);
                            //action = GameEvent.STEAL;
                            //addGameEvent();
                            break;
                        case 3:
                            selectedPlayer = arrayPlayer.get(3);
                            //action = GameEvent.STEAL;
                            //addGameEvent();
                            break;
                        case 4:
                            selectedPlayer = arrayPlayer.get(4);
                            //action = GameEvent.STEAL;
                            //addGameEvent();
                            break;
                        case 5:
                            selectedPlayer = null;
                            //Do nothing...
                    }
                });
        builder.create().show();
        selectedPlayer = temp;
    }

    private void createAssistAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        builder.setTitle("Made shot by " + playerName + " Assist from");
        ArrayList<Player> arrayPlayer =new ArrayList<Player>();
        ArrayList<String> arrayPlayerNames =new ArrayList<String>();
        for (Player player : homePlayers){
            if(player != selectedPlayer) {
                arrayPlayer.add(player);
                arrayPlayerNames.add(player.getName());
            }
        }

        CharSequence[] charPlayers = arrayPlayerNames.toArray(new CharSequence[arrayPlayerNames.size()]);
        Player temp = selectedPlayer;
        builder.setItems(new CharSequence[]{ charPlayers[0], charPlayers[1], charPlayers[2], charPlayers[3]},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            action = GameEvent.ASSIST;
                            selectedPlayer = arrayPlayer.get(0);
                            break;
                        case 1:
                            action = GameEvent.ASSIST;
                            selectedPlayer = arrayPlayer.get(1);
                            break;
                        case 2:
                            action = GameEvent.ASSIST;
                            selectedPlayer = arrayPlayer.get(2);
                            break;
                        case 3:
                            action = GameEvent.ASSIST;
                            selectedPlayer = arrayPlayer.get(3);
                            break;
                    }
                    addGameEvent();

                });
        builder.create().show();
        selectedPlayer = temp;
    }

    private int buttonId = 0;
    private Button createButton(Player player, int buttonId) {
        Button button = new Button(GameActivity.this);
        homePlayerButtons.add(button);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        button.setText(player.getName());
        button.setId(buttonId);
        button.setOnClickListener(view -> {
            selectedPlayer = homePlayers.get(buttonId);
        });
        return button;
    }

    private void createHomePlayerButtons(){
        LinearLayout layout = findViewById(R.id.HomeButtonLayout);
        int buttonId = 0;
        for (Player player : homePlayers)
            layout.addView(createButton(player, buttonId++));
        selectedPlayer = homePlayers.get(0);
    }


    private View.OnClickListener awayMadeShotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showMadeShotAway();
        }
    };

    private View.OnClickListener awayMissedShotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showMissedShotAway();
        }
    };

    private View.OnClickListener awayFoulListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createShootingFoulAlert(false);
        }
    };

    private View.OnClickListener awayTurnoverListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createStealAlert();
        }
    };

    public void showMissedShotAway(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Away team misses");
        builder.setCancelable(true);

        builder.setItems(new CharSequence[]{ "Shot missed", "Shot blocked" },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            createReboundAlert();
                            break;
                        case 1:
                            createBlockAlert();
                            break;
                    }
                });
        builder.create().show();
    }

    public void showMadeShotAway(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Away team scores");
        builder.setCancelable(true);

        builder.setItems(new CharSequence[]{ "Two point shot", "Three point shot" },
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            addScoreToAway(2);
                            break;
                        case 1:
                            addScoreToAway(3);
                            break;
                    }
                });
        builder.create().show();
    }

    public void addScoreToAway(int score){
        scoreAway += score;
        awayScore.setText(Integer.toString(scoreAway));
    }

    public void addScoreToHome(int score){
        scoreHome += score;
        homeScore.setText(Integer.toString(scoreHome));
    }


    /**
     * Klukka
     */
    private View.OnClickListener startPauseTimerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!timerRunning){
                mCountDownTimer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeft = millisUntilFinished;
                        upDateTimer();
                    }

                    @Override
                    public void onFinish() {
                        timerRunning = false;
                        startPauseTime.setText("Start");
                        startPauseTime.setVisibility(View.INVISIBLE);
                        setTime.setVisibility(View.VISIBLE);
                    }
                }.start();
                timerRunning = true;
                setTime.setVisibility(View.INVISIBLE);
                startPauseTime.setText("Pause");
            } else {
                mCountDownTimer.cancel();
                timerRunning = false;
                startPauseTime.setText("Start");
                setTime.setVisibility(View.VISIBLE);

            }

        }
    };

    private void upDateTimer(){
        int min = (int) (timeLeft / 1000) / 60;
        int sec = (int) (timeLeft / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d", min, sec);

        klukka.setText(timeLeft);
    }



    private View.OnClickListener setTimerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMin();
            upDateTimer();
            startPauseTime.setVisibility(View.VISIBLE);
        }
    };

    public void showPopupMin() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set time in minutes");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        // Center text inside EditText
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timeText = input.getText().toString();
                if(timeText.equals(null)){
                    showPopupMin();
                } else {
                    showPopupSec(timeText);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void showPopupSec(String min) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set time in seconds");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        // Center text inside EditText
        input.setGravity(Gravity.CENTER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timeText = input.getText().toString();
                if(timeText.equals(null)){
                    showPopupSec(min);
                } else{
                    doneInput(min, timeText);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void doneInput(String min, String sec) {
        long Lmin = Long.parseLong(min);
        long Lsec = Long.parseLong(sec);

        double temp = (double)Lsec/60;

        temp = Lmin + temp;

        temp *= 60000;
        timeLeft = (long)temp;

        Toast.makeText(this, "Setting clock to " + min + ": " + sec, Toast.LENGTH_LONG).show();

        upDateTimer();
    }

    /*
    *
    * Bakendi
    *
    */

    public void endGame() {
        endGameTask = new EndGameTask();
        endGameTask.execute((Void) null);
    }

    public void addGameEvent() {
        addGameEventTask = new AddGameEventTask();
        addGameEventTask.execute((Void) null);
    }

    public void loadGame() {
        LoadGameTask loadGameTask = new LoadGameTask();
        loadGameTask.execute((Void) null);
    }

    public class LoadGameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                game = (Game) Database.user.getActiveGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            homePlayers = game.getStartingLineup();
            runOnUiThread(() -> {
                createHomePlayerButtons();
            });
            return (Void) null;
        }
    }

    /**
     * Async endGame event
     *
     */
    public class EndGameTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return Database.game.endGame();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(g, MainActivity.class);
                g.startActivity(intent);
                finish();
            } else {
                System.out.println("Did nothing");
            }
        }
    }

    public GameEvent createGameEvent() throws Exception {
        GameEvent gameEvent = new GameEvent();
        gameEvent.setTimeOfEvent(timeOfEvent);
        gameEvent.setEventType(action);
        gameEvent.setLocation(location);
        gameEvent.setPlayerId(selectedPlayer.getId());
        return gameEvent;
    }
    public class AddGameEventTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean success = true;
            try {
                Database.game.addGameEvent(createGameEvent());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            runOnUiThread(() -> {
                Toast.makeText(GameActivity.this,
                        "Event - loc:" + GameEvent.LOCATION_NAMES[location] +
                                " act:" + GameEvent.ACTION_NAMES[action] +
                                " ply:" + selectedPlayer.getName()
                        , Toast.LENGTH_SHORT).show();
            });

            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                System.out.println("Successfully added gameEvent");
            } else {
                System.out.println("Did nothing");
            }
        }
    }

}





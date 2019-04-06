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

    // Timer
    private static final long TIME = 600000;
    private TextView klukka;
    private Button startPauseTime;
    private Button setTime;
    private CountDownTimer mCountDownTimer;
    private boolean timerRunning;
    String timeText;
    private Long timeLeft = TIME;

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

    private ArrayList<String> AwayplayersArr = new ArrayList<>(Arrays.asList(AwayplayersArray));
    private ArrayList<Button> AwayplayerButtons = new ArrayList<>();

    public void defineButtons(){
        findViewById(R.id.StartPauseTimer).setOnClickListener(startPauseTimerListener);
        findViewById(R.id.SetTimer).setOnClickListener(setTimerListener);
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

    private void createShotAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        String playerName = selectedPlayer.getName();
        builder.setCancelable(true);
        builder.setTitle("Action for " + playerName);

        builder.setItems(new CharSequence[]{"Made Shot", "Missed Shot", "Committed Foul", "Turnover", "Rebound", "Block", "Assist"},
                (dialog, which) -> {
                    switch (which) {
                        case 0: action = GameEvent.HIT     ; break;
                        case 1: action = GameEvent.MISS    ; break;
                        case 2: action = GameEvent.FOUL    ; break;
                        case 3: action = GameEvent.TURNOVER; break;
                        case 4: action = GameEvent.REBOUND ; break;
                        case 5: action = GameEvent.BLOCK   ; break;
                        case 6: action = GameEvent.ASSIST  ; break;
                    }
                    addGameEvent();
                });
        builder.create().show();
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

        defineButtons();
        createAwayPlayerButtons();
        upDateTimer();
        loadGame();
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

    private void createAwayPlayerButtons(){
        LinearLayout layout = findViewById(R.id.AwayButtonLayout);
        int id = 1;
        for(String s : AwayplayersArr){
            Button but = new Button(GameActivity.this);
            AwayplayerButtons.add(but);
            //optional: add your buttons to any layout if you want to see them in your screen
            but.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            but.setText(s);
            //Þarf að setja id fra database her!
            but.setId(id);
            but.setOnClickListener(buttonClickListener);
            layout.addView(but);
            id++;
        }
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button leikmadur = (Button)v;
            // setSelectedPlayer(leikmadur.getText().toString());
        }
    };

    // Klukka
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
                showPopupSec(timeText);
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
                doneInput(min, timeText);
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





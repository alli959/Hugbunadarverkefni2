package yolo.basket;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import yolo.basket.db.Database;
import yolo.basket.db.game.Game;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;
import yolo.basket.util.Location;

// TODO:
// Endgame button
// Add time to the the stuff
// Undo

public class GameActivity extends AppCompatActivity {

    private static final boolean HOME = true;
    private static final boolean AWAY = false;

    // Colors
    private int GRAY = Color.rgb(55, 55, 55);
    private int RED = Color.rgb(85, 45, 45);

    // Game
    private int action;
    private int location;
    private Game game;
    private boolean activeTeam = HOME;

    private int scoreHome = 0;
    private TextView homeScore;


    private static Player gameEventPlayer;
    private Player userSelectedPlayer;
    private Long gameClock = 600L;

    // Timer
    private static final long TIME = 600000;
    private TextView clockView;
    private Button startPauseTime;
    private Button setTime;
    private CountDownTimer mCountDownTimer;

    //Away team
    private TextView awayScore;
    private int scoreAway = 0;

    //Player
    private List<Player> players = new ArrayList<>();
    private ArrayList<Button> playerButtons = new ArrayList<>();

    // GameEvents
    private List<GameEvent> gameEvents;
    private ListView gameEventLog;

    private GameActivity gameActivity;
    private ImageView court;
    private EditText timerDialogInput;

    private static final long SEC_IN_MIN = 60;
    private static final long MS_IN_SEC = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameActivity = this;
        retrieveViews();

        bindTimerButtons();
        bindAwayTeamButtons();
        bindOnCourtClick();
        gameClock = 10 * SEC_IN_MIN * MS_IN_SEC;
        updateClockView();
        loadPlayers();
        loadGameEvents();
        bindOnLogClick();
    }

    private void retrieveViews() {
        court = findViewById(R.id.basketBallCourt);
        startPauseTime = findViewById(R.id.StartPauseTimer);
        setTime = findViewById(R.id.SetTimer);
        clockView = findViewById(R.id.Timer);
        gameEventLog = findViewById(R.id.gameEventLog);
        homeScore = findViewById(R.id.homeScore);
        awayScore = findViewById(R.id.awayScore);
    }

    public void bindTimerButtons(){
        findViewById(R.id.StartPauseTimer).setOnClickListener(startPauseTimerListener);
        findViewById(R.id.SetTimer).setOnClickListener(setTimerListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindOnCourtClick() {

        court.setOnTouchListener((view, event) -> {
            double x = event.getX() / view.getWidth();
            double y = event.getY() / view.getHeight();
            System.out.println(x + " "  + y);
            location = Location.getLocation(new Location(x, y, 0));
            gameEventPlayer = userSelectedPlayer;
            createHomeTeamAlert();
            return false;
        });
    }

    private void updateGameLog() {
        gameEventLog.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getLogData()
        ));
    }

    private List<String> getLogData() {
        return gameEvents.stream().map(gameEvent -> getLogStringForEvent(gameEvent)).collect(Collectors.toList());
    }

    private String getLogStringForEvent(GameEvent gameEvent) {
        String logString = "";
        logString += getClockString(gameEvent.getTimeOfEvent());
        logString += "\n";
        logString += getPlayerNameById(gameEvent.getPlayerId());
        logString += "\n";

        logString += GameEvent.ACTION_NAMES[gameEvent.getEventType()];

        int eventType = gameEvent.getEventType();
        if (
                eventType != GameEvent.REBOUND &&
                eventType != GameEvent.BLOCK &&
                eventType != GameEvent.ASSIST
        ) {
            logString += "\n";
            logString += GameEvent.LOCATION_NAMES[gameEvent.getLocation()];
        }

        return logString;
    }

    private String getPlayerNameById(Long id) {
        String name = "No name";
        for (Player player : players)
            if (player.getId().equals(id))
                name = player.getName();
        if (name.length() > 15) {
            name = name.substring(0, 15);
            name += "...";
        }
        return name;
    }

    private void bindOnLogClick() {
        gameEventLog.setOnItemClickListener(((parent, view, position, id) -> {
            GameEvent gameEvent = game.getGameEvents().get(position);
            System.out.println("Removing gameEvent");
            new RemoveGameEvent(gameEvent).execute((Void) null);
        }));
    }

    private void createHomeTeamAlert(){
        activeTeam = HOME;

        String title = "Action for " + gameEventPlayer.getName();
        String[] options = {
                "Made Shot",
                "Missed Shot",
                "Committed Foul",
                "Turnover / Steal"
        };

        createListDialog(title, options, (dialog, which) -> {
            switch (which) {
                case 0:
                    action = GameEvent.HIT;
                    addScoreToHome(GameEvent.locationPoints(location));
                    addGameEvent();
                    createAssistAlert();
                    break;
                case 1:
                    action = GameEvent.MISS;
                    addGameEvent();
                    createReboundAlert();
                    break;
                case 2:
                    createFoulAlert();
                    break;
                case 3:
                    createStealAlert();
                    break;
            }
        });
    }

    public void bindAwayTeamButtons(){
        findViewById(R.id.madeShotAway).setOnClickListener(v -> awayMadeShotAlert());
        findViewById(R.id.missedShotAway).setOnClickListener(v -> awayMissedAlert());
        findViewById(R.id.foulAway).setOnClickListener(v -> awayFoulAlert());
    }

    public void awayFoulAlert() {
        activeTeam = AWAY;
        createFoulAlert();
    }

    public void awayMissedAlert() {
        String title = "Away team misses";
        String[] options = { "Shot missed", "Shot blocked" };

        createListDialog(title, options, (dialog, which) -> {
            if (which == 0)
                createReboundAlert();
            else
                createBlockAlert();
        });
    }

    public void awayMadeShotAlert() {
        String title = "Away team scores";
        String[] options = { "Two point shot", "Three point shot" };

        createListDialog(title, options, (dialog, which) -> {
            if (which == 0)
                addScoreToAway(2);
            else
                addScoreToAway(3);
        });
    }

    private void createFoulAlert() {
        String title = getFoulAlertTitle();
        String[] options = { "Defensive foul", "Offensive foul(Turnover)" };

        createListDialog(title, options, (dialog, which) -> {
            if (which == 0)
                createFoulTypeDialog();
            if (activeTeam == HOME)
                addFoul();
        });
    }

    private void createFoulTypeDialog() {
        String title = getFoulAlertTitle();
        String[] options = { "Shooting foul", "Non shooting foul" };

        createListDialog(title, options, (dialog, which) -> {
            if (which == 0) {
                // Foul was made by active team -> switch teams for free throws
                activeTeam = !activeTeam;
                goToFreeThrows();
            }
        });
    }

    private String getFoulAlertTitle() {
        return activeTeam == HOME ? gameEventPlayer.getName() + " fouled a player, " : "Away team committed a foul";
    }

    private void addFoul() {
        action = GameEvent.FOUL;
        addGameEvent();
    }

    private void goToFreeThrows() {
        createFreeThrowAmountDialog();
    }

    private void createFreeThrowAmountDialog() {
        String title = "Shooting foul";
        String[] options = {
                "Basket made and 1 shot",
                "1 Shot",
                "2 shots",
                "3 Shots"
        };

        createListDialog(title, options, (dialog, which) -> {
            int numberOfFreeThrows = which;
            if (which == 0) {
                addScore(2);
                numberOfFreeThrows = 1;
            }
            if (activeTeam == HOME)
                selectPlayerForFreeThrows(numberOfFreeThrows);
            else
                startFreeThrows(numberOfFreeThrows);
        });
    }

    private void selectPlayerForFreeThrows(int numberOfFreeThrows){
        String title = "Shooting player";
        String[] options = asArray(getPlayerNames());
        createListDialog(title, options, (dialog, which) -> {
            gameEventPlayer = players.get(which);
            startFreeThrows(numberOfFreeThrows);
        });

    }

    private void startFreeThrows(int amount){
        while(amount-- != 0)
            createFreeThrowDialog();
    }

    private void createFreeThrowDialog() {
        String title = "Free throw";
        String[] options = { "Made", "Missed" };

        createListDialog(title, options, (dialog, which) -> {
            addScore(1);

            if (activeTeam == HOME) {
                action = which == 1 ? GameEvent.HIT : GameEvent.MISS;
                location = GameEvent.FREE_THROW;
                addGameEvent();
            }
        });
    }

    private void createReboundAlert() {
        List<String> optionsList = getPlayerNames();
        optionsList.add("Away team");
        optionsList.add("None");

        String title = gameEventPlayer.getName() + " missed the shot, rebound caught by...";
        String[] options = asArray(optionsList);

        Player temp = gameEventPlayer;
        createListDialog(title, options, (dialog, which) -> {
            if (which >= players.size())
                return;
            gameEventPlayer = players.get(which);
            action = GameEvent.REBOUND;
            addGameEvent();
        });
        gameEventPlayer = temp;
    }

    private void createBlockAlert() {
        String title = "Shot blocked by";
        String[] options = asArray(getPlayerNames());

        Player temp = gameEventPlayer;
        createListDialog(title, options, (dialog, which) -> {
            gameEventPlayer = players.get(which);
            action = GameEvent.BLOCK;
            addGameEvent();
        });
        gameEventPlayer = temp;
    }

    /**
     * TODO: GameEvent.Steal
     */

    private void createStealAlert(){
        String title = "Turnover / Steal";
        String[] options = { "Home team", "Away team", "Pass out of bounds" };

        Player temp = gameEventPlayer;
        createListDialog(title, options, (dialog, which) -> {
            if (which == 0) {
                // action = GameEvent.STEAL; // We want this
                action = GameEvent.TURNOVER;
                addGameEvent();
            }
        });
        gameEventPlayer = temp;
    }

    private void createAssistAlert() {
        String title = "Made shot by " + gameEventPlayer.getName() + " Assist from";
        String[] options = asArray(getPlayerNames());

        createListDialog(title, options, (dialog, which) -> {
            gameEventPlayer = players.get(which);
            action = GameEvent.ASSIST;
            addGameEvent();
        });
    }

    private void createListDialog(String title, String[] options, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setItems(options, onClickListener);
        builder.create();
        builder.show();
    }

    private List<String> getPlayerNames() {
        return players.stream().map(Player::getName).collect(Collectors.toList());
    }

    private String[] asArray(List<String> list) {
        return list.stream().toArray(String[]::new);
    }

    private void createPlayerButtons(){
        LinearLayout layout = findViewById(R.id.HomeButtonLayout);

        for (Player player : players) {
            Button button = createButton(player);
            layout.addView(button);
            playerButtons.add(button);
        }

        gameEventPlayer = players.get(0);
        userSelectedPlayer = gameEventPlayer;
    }

    private Button createButton(Player player) {
        Button button = new Button(GameActivity.this);
        playerButtons.add(button);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        button.setText(player.getName());
        button.setOnClickListener(view -> {
            userSelectedPlayer = player;
            deactivateButtons();
            button.setAlpha(1.0f);
        });
        return button;
    }

    private void deactivateButtons() {
        for (Button button : playerButtons)
            button.setAlpha(0.6f);
    }

    public void addScore(int score) {
        if (activeTeam == HOME)
            addScoreToHome(score);
        else
            addScoreToAway(score);
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
            if(mCountDownTimer == null){
                mCountDownTimer = createCountdown(gameClock);
                mCountDownTimer.start();
                setTime.setVisibility(View.INVISIBLE);
                startPauseTime.setText("Pause");
            } else {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
                startPauseTime.setText("Start");
                setTime.setVisibility(View.VISIBLE);
            }
        }
    };

    private CountDownTimer createCountdown(long time) {
        return new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                gameClock = millisUntilFinished;
                updateClockView();
            }

            @Override
            public void onFinish() {
                startPauseTime.setText("Start");
                startPauseTime.setVisibility(View.INVISIBLE);
                setTime.setVisibility(View.VISIBLE);
                mCountDownTimer = null;
            }
        };
    }

    private void updateClockView(){
        String timeLeft = getClockString(gameClock);
        clockView.setText(timeLeft);
    }

    private String getClockString(long clockValue) {
        int min = (int) (clockValue / 1000) / 60;
        int sec = (int) (clockValue / 1000) % 60;
        return String.format(Locale.getDefault(),"%02d:%02d", min, sec);
    }

    private View.OnClickListener setTimerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMin();
            updateClockView();
            startPauseTime.setVisibility(View.VISIBLE);
        }
    };

    public void showPopupMin() {
        createDialogInput("Set minutes", ((dialog, which) -> {
            String minutes = timerDialogInput.getText().toString();
            showPopupSec(minutes);
        }));
    }


    public void showPopupSec(String minutes) {
        createDialogInput("Set time in seconds", (dialog, which) -> {
            String seconds = timerDialogInput.getText().toString();
            doneInput(minutes, seconds);
        });
    }

    public void createDialogInput(String title, DialogInterface.OnClickListener onSubmit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);

        timerDialogInput = new EditText(this);
        timerDialogInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        timerDialogInput.setGravity(Gravity.CENTER);
        builder.setView(timerDialogInput);

        builder.setPositiveButton("OK", onSubmit);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void doneInput(String min, String sec) {
        gameClock = (long) ((Integer.parseInt(min) * 60 + Integer.parseInt(sec)) * 1000);
        Toast.makeText(this, "Setting gameClock to " + min + ": " + sec, Toast.LENGTH_LONG).show();
        updateClockView();
    }

    public void endGame() {
        new EndGameTask().execute((Void) null);
        this.finish();
    }

    public void addGameEvent() {
        new AddGameEventTask().execute((Void) null);
    }

    public void loadGameEvents() {
        new LoadGameEventsTask().execute((Void) null);
    }

    public void loadPlayers() {
        new LoadPlayersTask().execute((Void) null);
    }



    public class RemoveGameEvent extends AsyncTask<Void, Void, Void> {
        private final GameEvent gameEvent;

        RemoveGameEvent(GameEvent gameEvent) {
            this.gameEvent = gameEvent;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Database.game.removeGameEvent(gameEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoadGameEventsTask().execute((Void) null);
            return (Void) null;
        }
    }


    public class LoadGameEventsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                game = (Game) Database.user.getActiveGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameEvents = game.getGameEvents();
            runOnUiThread(() -> {
                updateGameLog();
            });
            return (Void) null;
        }
    }

    public class LoadPlayersTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                game = (Game) Database.user.getActiveGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            players = game.getStartingLineup();
            runOnUiThread(() -> {
                createPlayerButtons();
            });
            return (Void) null;
        }
    }

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
                Intent intent = new Intent(gameActivity, MainActivity.class);
                gameActivity.startActivity(intent);
                finish();
            }
        }
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
            loadGameEvents();
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

    public GameEvent createGameEvent() throws Exception {
        GameEvent gameEvent = new GameEvent();
        gameEvent.setTimeOfEvent(gameClock);
        gameEvent.setEventType(action);
        gameEvent.setLocation(location);
        gameEvent.setPlayerId(gameEventPlayer.getId());
        return gameEvent;
    }
}





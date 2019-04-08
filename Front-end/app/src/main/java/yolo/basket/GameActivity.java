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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import yolo.basket.db.Database;
import yolo.basket.db.game.Game;
import yolo.basket.db.gameEvent.GameEvent;
import yolo.basket.db.player.Player;
import yolo.basket.util.Actions;
import yolo.basket.util.Location;

// TODO:
// Endgame button
// Add time to the the stuff
// Undo

public class GameActivity extends AppCompatActivity {

    // Game
    private int action;
    private int location;
    private Game game;
    private static Player selectedPlayer;
    private Long gameClock = 600L;

    // Timer
    private static final long TIME = 600000;
    private TextView clockView;
    private Button startPauseTime;
    private Button setTime;
    private CountDownTimer mCountDownTimer;
    private boolean timerRunning;
    private Long timeLeft = TIME;

    //Player
    private List<Player> players = new ArrayList<>();
    private ArrayList<Button> playerButtons = new ArrayList<>();

    // GameEvents
    private List<GameEvent> gameEvents;
    private ListView gameEventLog;

    private GameActivity gameActivity;

    private ImageView court;

    // Gives the GameEvent location type a location on the image
    // Used to calculate which one is closest for approximate solution
    private static final List<Location> LOCATIONS = new ArrayList<>(Arrays.asList(
            new Location(0.09, 0.18, GameEvent.LEFT_CORNER),
            new Location(0.90, 0.18, GameEvent.RIGHT_CORNER),
            new Location(0.86, 0.59, GameEvent.RIGHT_WING),
            new Location(0.66, 0.64, GameEvent.LEFT_WING),
            new Location(0.74, 0.16, GameEvent.RIGHT_SHORT),
            new Location(0.74, 0.27, GameEvent.RIGHT_SHORT),
            new Location(0.26, 0.16, GameEvent.LEFT_SHORT),
            new Location(0.26, 0.27, GameEvent.LEFT_SHORT),
            new Location(0.50, 0.29, GameEvent.LAY_UP),
            new Location(0.50, 0.50, GameEvent.TOP),
            new Location(0.31, 0.79, GameEvent.RIGHT_TOP),
            new Location(0.66, 0.80, GameEvent.LEFT_TOP)
    ));
    private EditText timerDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameActivity = this;
        retrieveViews();

        bindOnCourtClick();
        bindTimerButtons();
        updateClockView(6000);
        loadPlayers();
        loadGameEvents();
        bindOnLogClick();
    }

    public void bindTimerButtons(){
        findViewById(R.id.StartPauseTimer).setOnClickListener(startPauseTimerListener);
        findViewById(R.id.SetTimer).setOnClickListener(setTimerListener);
    }

    private void retrieveViews() {
        court = findViewById(R.id.basketBallCourt);
        startPauseTime = findViewById(R.id.StartPauseTimer);
        setTime = findViewById(R.id.SetTimer);
        clockView = findViewById(R.id.Timer);
        gameEventLog = findViewById(R.id.gameEventLog);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindOnCourtClick() {
        court.setOnTouchListener((view, event) -> {
            double x = event.getX() / view.getWidth();
            double y = event.getY() / view.getHeight();
            location = getLocation(new Location(x, y, 0));
            createShotAlert();
            return false;
        });
    }

    private int getLocation(Location clickLocation) {
        Location closestLocation = LOCATIONS.stream().reduce(
                (loc1, loc2) -> clickLocation.distanceTo(loc1) < clickLocation.distanceTo(loc2) ?
                        loc1 : loc2).get();
        return closestLocation.getCourtLocation();
    }


    private void updateGameLog() {
        gameEventLog.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getLogData()
        ));
    }

    private void bindOnLogClick() {
        gameEventLog.setOnItemClickListener(((parent, view, position, id) -> {
            game.getGameEvents().remove(position);
            new UpdateGameTask().execute((Void) null);
        }));
    }

    private List<String> getLogData() {
       return gameEvents.stream().map(gameEvent -> {
            String logString = "";
            logString += gameEvent.getTimeOfEvent() / 1000;
            logString += GameEvent.ACTION_NAMES[gameEvent.getEventType()];
            logString += GameEvent.LOCATION_NAMES[gameEvent.getLocation()];
            return logString;
        }).collect(Collectors.toList());
    }

    private void createPlayerButtons(){
        LinearLayout layout = findViewById(R.id.HomeButtonLayout);
        int buttonId = 0;
        for (Player player : players)
            layout.addView(createButton(player, buttonId++));
        selectedPlayer = players.get(0);
    }

    private Button createButton(Player player, int buttonId) {
        Button button = new Button(GameActivity.this);
        playerButtons.add(button);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        button.setText(player.getName());
        button.setId(buttonId);
        button.setOnClickListener(view -> {
            selectedPlayer = players.get(buttonId);
        });
        return button;
    }

    private void createShotAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Action for " + selectedPlayer.getName());
        builder.setItems(Actions.getNames(),
                (dialog, which) -> {
                    action = Actions.values()[which].getAction();
                    if (action == Actions.Missed.getAction())
                        createReboundAlert();
                    addGameEvent();
                });
        builder.create().show();
    }

    private void createReboundAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Rebound");
        builder.setItems(players.stream().map(Player::getName).toArray(String[]::new),
                (dialog, which) -> {
                    action = GameEvent.REBOUND;
                    selectedPlayer = players.get(which);
                    addGameEvent();
                });
        builder.create().show();
    }

    // Klukka
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
                updateClockView(millisUntilFinished);
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

    private void updateClockView(long millisUntilFinished){
        int min = (int) (millisUntilFinished / 1000) / 60;
        int sec = (int) (millisUntilFinished / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d", min, sec);
        clockView.setText(timeLeft);
    }

    private View.OnClickListener setTimerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopupMin();
            updateClockView(gameClock);
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
        updateClockView(gameClock);
    }

    public void endGame() {
        new EndGameTask().execute((Void) null);
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



    public class UpdateGameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                game = (Game) Database.game.save(game);
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

            runOnUiThread(() -> {
                Toast.makeText(GameActivity.this,
                        "Event - loc:" + GameEvent.LOCATION_NAMES[location] +
                                " act:" + GameEvent.ACTION_NAMES[action] +
                                " ply:" + selectedPlayer.getName()
                        , Toast.LENGTH_SHORT).show();
            });
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
        gameEvent.setPlayerId(selectedPlayer.getId());
        return gameEvent;
    }
}





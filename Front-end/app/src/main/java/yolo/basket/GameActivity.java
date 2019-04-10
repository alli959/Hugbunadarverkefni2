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

    private int scoreHome = 0;
    private TextView homeScore;


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

    //Away team
    private Button madeShotAway;
    private Button missedShotAway;
    private Button foulAway;
    private Button turnoverAway;
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



    public void defineButtons(){
        findViewById(R.id.madeShotAway).setOnClickListener(awayMadeShotListener);
        findViewById(R.id.foulAway).setOnClickListener(awayFoulListener);
        findViewById(R.id.missedShotAway).setOnClickListener(awayMissedShotListener);
        findViewById(R.id.turnoverAway).setOnClickListener(awayTurnoverListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameActivity = this;
        retrieveViews();

        bindOnCourtClick();
        bindTimerButtons();
        defineButtons();
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
        homeScore = (TextView) findViewById(R.id.homeScore);
        awayScore = (TextView) findViewById(R.id.awayScore);
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
            GameEvent gameEvent = game.getGameEvents().get(position);
            System.out.println("Removing gameEvent");
            new RemoveGameEvent(gameEvent).execute((Void) null);
        }));
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





package yolo.basket;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import yolo.basket.canvas.CanvasView;
import yolo.basket.db.Database;
import yolo.basket.db.gameEvent.GameEvent;

import static yolo.basket.db.Database.game;
import static yolo.basket.db.Database.login;

public class gameActivity extends AppCompatActivity {

    // Game
    private EndGameTask endGameTask;
    private AddGameEventTask addGameEventTask;
    private String action;
    private String location;
    private long timeOfEvent;
    private Long selectedPlayerID;

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
    private static String selectedPlayer = "";
    private String[] HomeplayersArray = {
            "LeBron James",
            "Kyle Kuzma",
            "Lonzo Ball",
            "JaVale McGee",
            "Brandon Ingram"
    };

    private ArrayList<String> HomeplayersArr = new ArrayList<String>(Arrays.asList(HomeplayersArray));

    private ArrayList<Button> HomeplayerButtons = new ArrayList<Button>();

    private String[] AwayplayersArray = {
            "Stephen Curry",
            "Klay Thompson",
            "Kevin Durant",
            "Draymond Greeen",
            "DeMarcus Cousins"
    };

    private ArrayList<String> AwayplayersArr = new ArrayList<String>(Arrays.asList(AwayplayersArray));

    private ArrayList<Button> AwayplayerButtons = new ArrayList<Button>();

    public String getSelectedPlayer() {
        Log.d("110495", selectedPlayer);
        return selectedPlayer;
    }

    public void setSelectedPlayer(String selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
    }



    public void defineButtons(){
        //findViewById(R.id.leikmadur1).setOnClickListener(buttonClickListener);
        //findViewById(R.id.leikmadur2).setOnClickListener(buttonClickListener);
        findViewById(R.id.StartPauseTimer).setOnClickListener(startPauseTimerListener);
        findViewById(R.id.SetTimer).setOnClickListener(setTimerListener);
    }

    gameActivity g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        g = this;

        startPauseTime = (Button) findViewById(R.id.StartPauseTimer);
        setTime = (Button) findViewById(R.id.SetTimer);

        klukka = (TextView) findViewById(R.id.Timer);

        defineButtons();

        createHomePlayerButtons();
        createAwayPlayerButtons();

        upDateTimer();

    }

    private void createHomePlayerButtons(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.HomeButtonLayout);
        int id = 1;
        for(String s : HomeplayersArr){
                Button but = new Button(gameActivity.this);
                HomeplayerButtons.add(but);
                //optional: add your buttons to any layout if you want to see them in your screen
                but.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                but.setText(s);
                but.setId(id);
                layout.addView(but);
                id++;
        }
    }

    private void createAwayPlayerButtons(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.AwayButtonLayout);
        int id = 1;
        for(String s : AwayplayersArr){
            Button but = new Button(gameActivity.this);
            AwayplayerButtons.add(but);
            //optional: add your buttons to any layout if you want to see them in your screen
            but.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            but.setText(s);
            but.setId(id);
            layout.addView(but);
            id++;
        }
    }

    /*private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.leikmadur1:
                    //Action
                    Button leikmadur1 = (Button)v;
                    setSelectedPlayer(leikmadur1.getText().toString());
                    Log.d("110495", selectedPlayer);
                    break;
                case R.id.leikmadur2:
                    //Action
                    Button leikmadur2 = (Button)v;
                    setSelectedPlayer(leikmadur2.getText().toString());
                    Log.d("110495", selectedPlayer);
                break;
            }
        }
    };*/

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
        gameEvent.setEventType(GameEvent.getEventTypeByName(action));
        gameEvent.setLocation(GameEvent.getLocationByName(location));
        gameEvent.setPlayerId(selectedPlayerID);
        return gameEvent;
    }

    /**
     * Async addGameEvent
     */
    public class AddGameEventTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return Database.game.addGameEvent(createGameEvent());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
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





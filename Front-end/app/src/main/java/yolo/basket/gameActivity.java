package yolo.basket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

import yolo.basket.canvas.CanvasView;
import yolo.basket.db.Database;
import yolo.basket.db.gameEvent.GameEvent;

import static yolo.basket.db.Database.game;
import static yolo.basket.db.Database.login;

public class gameActivity extends AppCompatActivity {

    private EndGameTask endGameTask;
    private AddGameEventTask addGameEventTask;
    private String action;
    private String location;
    private long timeOfEvent;

    private String selectedPlayer;

    private Long selectedPlayerID;

    private TextView alertTextView;
    private ImageView court;

    private CanvasView canvas;

    public void defineButtons(){
        findViewById(R.id.leikmadur1).setOnClickListener(buttonClickListener);
        findViewById(R.id.leikmadur2).setOnClickListener(buttonClickListener);

    }

    gameActivity g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        g = this;

        alertTextView = (TextView) findViewById(R.id.AlertTextView);
        canvas = (CanvasView) findViewById(R.id.basketBallCourtCanvas);

        court = (ImageView) findViewById(R.id.basketBallCourt);


    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.leikmadur1:
                    //Action
                    Button leikmadur1 = (Button) findViewById(R.id.leikmadur1);
                    selectedPlayer = (String)leikmadur1.getText();
                    break;
                case R.id.leikmadur2:
                    //Action
                    Button leikmadur2 = (Button) findViewById(R.id.leikmadur2);
                    selectedPlayer = (String)leikmadur2.getText();
                    break;
            }
        }
    };



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





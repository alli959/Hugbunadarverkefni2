package yolo.basket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import yolo.basket.db.Database;
import yolo.basket.db.game.Game;
import yolo.basket.db.gameEvent.GameEvent;

import static yolo.basket.db.Database.game;

public class gameActivity extends AppCompatActivity {

    private Game currentGame;

    private EndGameTask endGameTask;
    private AddGameEventTask addGameEventTask;
    private String action;
    private String location;
    private long timeOfEvent;

    private long selectedPlayer;
    private TextView alertTextView;
    private ImageButton court;

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
        court = (ImageButton) findViewById(R.id.basketBallCourt);

        court.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(gameActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Action for " + selectedPlayer);
                //builder.setMessage("This is an Alert Dialog Message: " + leikmadur1.getText());

                builder.setItems(new CharSequence[]
                                {"action 1", "action 2", "action 3", "action 4"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                // TODO búa til action föll sem tekur inn id af player og uppfærir rétt action í db.
                                switch (which) {
                                    case 0:
                                        Toast.makeText(gameActivity.this, "action 1", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(gameActivity.this, "action 2", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(gameActivity.this, "action 3", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Toast.makeText(gameActivity.this, "action 4", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                builder.create().show();

            }

        });
    }

    private void startGame() {
        // Thetta keyrir thegar leikurinn er kominn inn i currentGame
        System.out.println("Party has started!!!! WOOOO!!!");
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
        gameEvent.setPlayerId(selectedPlayer);
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

    /**
     * Async addGameEvent
     */
    public class GetActiveGame extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                currentGame = Database.game.getActiveGame();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                startGame();
            } else {
                g.finishActivity(0);
            }
        }
    }

}





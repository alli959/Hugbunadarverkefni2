package yolo.basket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import yolo.basket.db.team.Team;



/*
TeamActivity is a activity containing 2 fragments, witch are substituted by actions
 */




/*TODO

connect preGameFragment to db.
 */

public class TeamActivity extends AppCompatActivity implements TeamRightFragment.FragmentRightListener, TeamLeftFragment.FragmentLeftListener, PlayerRightFragment.FragmentPlayerListener, PreGameFragment.FragmentPreGameListener {
    private TeamLeftFragment teamLeftFragment;
    private TeamRightFragment teamRightFragment;
    private PlayerRightFragment playerRightFragment;
    private PreGameFragment preGameFragment;
    private PreGameFragment pregame;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Button startGame;


    private boolean isPreGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        //get all the fragments
        startGame = findViewById(R.id.startGame);
        teamLeftFragment = new TeamLeftFragment();
        teamRightFragment = new TeamRightFragment();
        playerRightFragment = new PlayerRightFragment();
        preGameFragment = new PreGameFragment();

        //place default fragments to activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.leftfragment, teamLeftFragment)
                .replace(R.id.rightfragment, teamRightFragment)
                .commit();



    }


    //checks witch view should appear,
    //depending on radio buttons
    public void checker(View v){
        FrameLayout startGameLayout = (FrameLayout) findViewById(R.id.rightfragment);
        radioGroup = findViewById(R.id.radio_group);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(radioId);
        String viewpoint = (String) radioButton.getText();
        if(viewpoint.equals("Game")){
            startGameLayout.setVisibility(View.GONE);
            teamLeftFragment.seeCreateTeamButton(false);
            isPreGame = true;
        }
        else{
            startGameLayout.setVisibility(View.GONE);
            teamLeftFragment.seeCreateTeamButton(true);
            isPreGame = false;
        }

    }


    //start Game Button

    public void startGame(View view){
        Intent intent = new Intent(this, gameActivity.class);
        startActivity(intent);
    }


    //interfaces from the fragments
    //where the substitution of the fragments happen
    @Override
    public void onRightFragmentInput(CharSequence input) {
        teamLeftFragment.updateTeamNames();
    }


    /*
    When button Create Team is clicked in TeamLeftFragment
    Puts TeamRightFragment to the right side of the activity
     */
    @Override
    public void showRightTeamView(boolean value) {
        FrameLayout teamRightLayout = (FrameLayout)findViewById(R.id.rightfragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rightfragment, teamRightFragment)
                .commit();
        if(value){
            teamRightLayout.setVisibility(View.VISIBLE);
        }
        else{
            teamRightLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void showRightPlayerView(boolean value) {

    }

    @Override
    public void getTeamName(CharSequence value) {

    }


    /*
    checks witch view should appear on the right
    when team is clicked

    preGameFragment if radio Buttion is on game
    playerRightFragment if radio button is on creation

     */
    @Override
    public void showRightPlayerView(boolean value, Long teamId, String teamName) {

        if(isPreGame){
            Log.d("this", "showRightPlayerView: ");
            FrameLayout preGameLayout = (FrameLayout) findViewById(R.id.rightfragment);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rightfragment, preGameFragment)
                    .commit();

            if(value){
                preGameLayout.setVisibility(View.VISIBLE);
            }
            else{
                preGameLayout.setVisibility(View.GONE);
            }
        }


        else {


            FrameLayout playerRightLayout = (FrameLayout) findViewById(R.id.rightfragment);
            Bundle bundle = new Bundle();
            bundle.putLong("teamId", teamId);
            bundle.putString("teamName", teamName);
            playerRightFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .detach(playerRightFragment)
                    .attach(playerRightFragment)
                    .replace(R.id.rightfragment, playerRightFragment)
                    .commit();
            if (value) {

                playerRightLayout.setVisibility(View.VISIBLE);
            } else {
                playerRightLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void isPregameView(boolean value) {

    }


    /*
    When Button StartGame is Clicked in preGameView fragment.
    opens a new activity gameActivity

    TODO
    send values of starters and bench to gameActivity
     */
    @Override
    public void showStartGameView(boolean value) {
        FrameLayout startGameLayout = (FrameLayout) findViewById(R.id.rightfragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rightfragment, preGameFragment)
                .commit();
        if(value){


            startGameLayout.setVisibility(View.VISIBLE);
        }
        else{
            startGameLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPlayerFragmentInput(CharSequence input) {

    }

}

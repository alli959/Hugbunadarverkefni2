package yolo.basket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import yolo.basket.db.team.Team;

public class TeamActivity extends AppCompatActivity implements TeamRightFragment.FragmentRightListener, TeamLeftFragment.FragmentLeftListener, PlayerRightFragment.FragmentPlayerListener, PreGameFragment.FragmentPreGameListener {
    private TeamLeftFragment teamLeftFragment;
    private TeamRightFragment teamRightFragment;
    private PlayerRightFragment playerRightFragment;
    private PreGameFragment preGameFragment;
    private PreGameFragment pregame;

    private RadioGroup radioGroup;
    private RadioButton radioButton;


    private boolean isPreGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamLeftFragment = new TeamLeftFragment();
        teamRightFragment = new TeamRightFragment();
        playerRightFragment = new PlayerRightFragment();
        preGameFragment = new PreGameFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.leftfragment, teamLeftFragment)
                .replace(R.id.rightfragment, teamRightFragment)
                .commit();



    }


    public void checker(View v){
        FrameLayout startGameLayout = (FrameLayout) findViewById(R.id.rightfragment);
        radioGroup = findViewById(R.id.radio_group);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(radioId);
        String viewpoint = (String) radioButton.getText();
        Log.d("viewPoint",  viewpoint);
        if(viewpoint.equals("Game")){
            startGameLayout.setVisibility(View.GONE);
            isPreGame = true;
        }
        else{
            startGameLayout.setVisibility(View.GONE);

            isPreGame = false;
        }

    }


    @Override
    public void onRightFragmentInput(CharSequence input) {
        teamLeftFragment.updateTeamNames();
    }

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

    @Override
    public void showRightPlayerView(boolean value, Long teamId, String teamName) {

        if(isPreGame){
            Log.d("this", "showRightPlayerView: ");
            FrameLayout preGameLayout = (FrameLayout) findViewById(R.id.rightfragment);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rightfragment, preGameFragment)
                    .commit();

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
        if(value){
            isPreGame = true;
        }
        else{
            isPreGame = false;
        }
    }

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

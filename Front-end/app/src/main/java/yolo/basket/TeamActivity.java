package yolo.basket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TeamActivity extends AppCompatActivity implements TeamRightFragment.FragmentRightListener, TeamLeftFragment.FragmentLeftListener, PlayerRightFragment.FragmentPlayerListener, PreGameFragment.FragmentPreGameListener {
    private TeamLeftFragment teamLeftFragment;
    private TeamRightFragment teamRightFragment;
    private PlayerRightFragment playerRightFragment;
    private PreGameFragment pregame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);


        teamLeftFragment = new TeamLeftFragment();
        teamRightFragment = new TeamRightFragment();
        playerRightFragment = new PlayerRightFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.leftfragment, teamLeftFragment)
                .replace(R.id.rightfragment, teamRightFragment)
                .commit();









    }

    @Override
    public void onRightFragmentInput(CharSequence input) {
        teamLeftFragment.updateTeamNames(input);

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
        FrameLayout playerRightLayout = (FrameLayout) findViewById(R.id.rightfragment);
        TextView textView = (TextView) playerRightLayout.findViewById(R.id.rightPlayerHeader);
        Log.d("textView", "showRightPlayerView() returned: " + textView);
        //textView.setText(String.valueOf("hello bitch"));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rightfragment, playerRightFragment)
                .commit();
        if(value){


            playerRightLayout.setVisibility(View.VISIBLE);
        }
        else{
            playerRightLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void getTeamName(CharSequence value) {

        //playerRightFragment.setTeamName(value);

    }

    @Override
    public void onPlayerFragmentInput(CharSequence input) {

    }
}

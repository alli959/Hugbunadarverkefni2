package yolo.basket.teamActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import yolo.basket.GameActivity;
import yolo.basket.R;

/*
TeamActivity is a activity containing 2 fragments, witch are substituted by actions
TODO
connect preGameFragment to db.
 */

public class TeamActivity extends AppCompatActivity implements CreateTeamFragment.FragmentRightListener, TeamSelectFragment.FragmentLeftListener, EditTeamPlayersFragment.FragmentPlayerListener, PreGameFragment.FragmentPreGameListener {

    private FrameLayout rightContainer;
    private FrameLayout leftSideLayout;

    private TeamSelectFragment teamLeftFragment;
    private CreateTeamFragment teamRightFragment;
    private EditTeamPlayersFragment playerRightFragment;
    private PreGameFragment preGameFragment;

    private boolean showRightFragment;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Button startGame;
    private boolean isPreGame = false;

    private Long teamId = -1L;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        //get all the fragments
        startGame = findViewById(R.id.startGame);
        teamLeftFragment = new TeamSelectFragment();
        teamRightFragment = new CreateTeamFragment();
        playerRightFragment = new EditTeamPlayersFragment();
        preGameFragment = new PreGameFragment();
        showRightFragment = false;

        // Have no idea if this is correct
        rightContainer = (FrameLayout) findViewById(R.id.rightfragment);

        //place default fragments to activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.leftfragment, teamLeftFragment)
                .replace(R.id.rightfragment, teamRightFragment)
                .commit();
    }

    protected void updateLayout() {
        rightContainer.setVisibility(showRightFragment ? View.VISIBLE : View.GONE);
        if (showRightFragment)
            populateRightContainer();
    }

    // Here we use the class variables to determine the next layout
    protected void populateRightContainer() {
        boolean isPregame = teamLeftFragment.getIsPregame();
        System.out.println("isPregame: " + isPregame);
        Fragment nextRightFragement = isPregame ? preGameFragment : playerRightFragment;
        Bundle bundle = new Bundle();
        bundle.putLong("teamId", teamId);
        bundle.putString("teamName", teamName);

        nextRightFragement.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rightfragment, nextRightFragement)
                .commit();
        rightContainer.setVisibility(View.VISIBLE);
    }

    // Team with ID = -1 will never exist
    @Override
    public void updateRightSideLayout(Long teamId, String teamName) {
        // If the same team is picked twice:
        //      no render right
        boolean shouldClose = this.teamId.equals(teamId) && showRightFragment;
        showRightFragment = shouldClose ? false : true;

        // Set teamId to (1- == not selected) or the id of the team clicked
        this.teamId = shouldClose ? -1L : teamId;

        this.teamName = teamName;
        updateLayout();
    }

    //checks witch view should appear,
    //depending on radio buttons
    public void checker(View v){
        System.out.println("Nothing");
        /*
        FrameLayout startGameLayout = (FrameLayout) findViewById(R.id.rightfragment);
        radioGroup = findViewById(R.id.radio_group);
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
        */
    }

    //start Game Button handler
    public void startGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    //interfaces from the fragments
    //where the substitution of the fragments happen
    @Override
    public void onRightFragmentInput(CharSequence input) {
        teamLeftFragment.updateTeamNames();
    }

    /*
    When button Create Team is clicked in TeamSelectFragment
    Puts CreateTeamFragment to the right side of the activity
     */
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

    /*
    checks witch view should appear on the right
    when team is clicked

    preGameFragment if radio Buttion is on game
    playerRightFragment if radio button is on creation

     */

    public void isPregameView(boolean value) {

    }

    /*
    When Button StartGame is Clicked in preGameView fragment.
    opens a new activity GameActivity

    TODO
    send values of starters and bench to GameActivity
     */

    public void showStartGameView(boolean shouldShow) {
        FrameLayout startGameLayout = (FrameLayout) findViewById(R.id.rightfragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rightfragment, preGameFragment)
                .commit();

        if(shouldShow) startGameLayout.setVisibility(View.VISIBLE);
        else           startGameLayout.setVisibility(View.GONE);
    }


    @Override
    public void onPlayerFragmentInput(CharSequence input) {

    }

}

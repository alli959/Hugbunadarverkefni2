package yolo.basket.teamActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import yolo.basket.R;

/*
TeamActivity is a activity containing 2 fragments, witch are substituted by actions
TODO
connect preGameFragment to db.
 */

public class TeamActivity extends AppCompatActivity implements CreateTeamFragment.CreateTeamListener, TeamSelectFragment.TeamSelectListener, EditTeamPlayersFragment.FragmentPlayerListener, PreGameFragment.FragmentPreGameListener {

    private FrameLayout rightContainer;

    private TeamSelectFragment teamSelectFragment;
    private CreateTeamFragment createTeamFragment;
    private EditTeamPlayersFragment editPlayersFragment;
    private PreGameFragment preGameFragment;

    private Fragment nextRightFragment;
    private Fragment nextLeftFragment;
    private boolean showRightFragment;

    private static final Long NULL_TEAM = -1L;

    private Long teamId = NULL_TEAM;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        retrieveViews();
        createFragments();

        nextLeftFragment = teamSelectFragment;
        nextRightFragment = new Fragment();
        showRightFragment = false;
        updateFragments();
    }

    // Team with ID = -1 will never exist
    @Override
    public void onTeamSelected(Long teamId) {
        if (isSameTeamAsCurrentTeam(teamId))
            showRightFragment = !showRightFragment;
        else
            showRightFragment = true;
        this.teamId = teamId;
        System.out.println(showRightFragment);
        if (teamSelectFragment.isPreGame())
            showPregameFragment();
        else
            showEditPlayersFragment();
    }

    @Override
    public void onCreateTeam() {
        teamSelectFragment.updateTeamNames();
    }

    private void retrieveViews() {
        rightContainer = findViewById(R.id.rightContainer);
    }

    private void createFragments() {
        teamSelectFragment = new TeamSelectFragment();
        createTeamFragment = new CreateTeamFragment();
        editPlayersFragment = new EditTeamPlayersFragment();
        preGameFragment = new PreGameFragment();
    }

    @Override
    public void showCreateTeamFragment() {
        nextRightFragment = createTeamFragment;
        renderLayout();
    }

    private void showEditPlayersFragment() {
        nextRightFragment = editPlayersFragment;
        renderLayout();
    }

    private void showPregameFragment() {
        nextRightFragment = preGameFragment;
        renderLayout();
    }

    private void renderLayout() {
        if (!showRightFragment) {
            nextRightFragment = new Fragment();
            rightContainer.setVisibility(View.GONE);
        } else {
            rightContainer.setVisibility(View.VISIBLE);
        }
        updateFragments();
    }

    private boolean isSameTeamAsCurrentTeam(Long teamId) {
        return this.teamId.equals(teamId);
    }

    private void updateFragments() {
        System.out.println("Updating fragments");
        nextRightFragment.setArguments(createBundle());
        runOnUiThread(() -> {
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.leftContainer, nextLeftFragment)
                .replace(R.id.rightContainer, nextRightFragment)
                .commit();
                });
    }

    private Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("teamId", teamId);
        bundle.putString("teamName", teamName);
        return bundle;
    }

    @Override
    public void onPlayerFragmentInput(CharSequence input) {

    }

}

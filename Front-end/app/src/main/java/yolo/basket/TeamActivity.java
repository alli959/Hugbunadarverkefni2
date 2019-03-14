package yolo.basket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class TeamActivity extends AppCompatActivity implements TeamRightFragment.FragmentRightListener, TeamLeftFragment.FragmentLeftListener {
    private TeamLeftFragment teamLeftFragment;
    private TeamRightFragment teamRightFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        teamLeftFragment = new TeamLeftFragment();
        teamRightFragment = new TeamRightFragment();

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
    public void onLeftFragmentInput(CharSequence input) {

    }
}

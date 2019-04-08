package yolo.basket;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import yolo.basket.db.Database;
import yolo.basket.db.player.Player;
import yolo.basket.db.stats.Stats;
import yolo.basket.db.team.Team;

public class StatsActivity extends AppCompatActivity {

    // Add this
    // https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/

    // I'm thinking about making a foldable drop-down list
    // -- player --
    //   >>  *click*
    // -- player --
    //      player1
    //      player2
    //      ...
    //
    // With buttons that open a dialog with stats

    private List<Team> teams;
    private List<String> teamNames = new ArrayList<>();

    private StatsViewExpandableListAdapter teamNameAdapter;
    private ExpandableListView teamListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats2);

        retrieveViews();
        getTeamsFromDatabase();
        bindOnItemClick();
    }

    private void retrieveViews() {
        teamListView = findViewById(R.id.stats_view_main);
    }

    private void bindOnItemClick() {
        teamListView.setOnChildClickListener(((parent, v, groupPosition, childPosition, id) -> {
            Player player = teams.get(groupPosition).getPlayers().get(childPosition);
            getStatsAndDisplay(player);
            return false;
        }));
    }

    private void renderTeamNames() {
        HashMap<String, List<String>> teamsAndPlayers = new HashMap<>();
        for (Team team : teams)
            teamsAndPlayers.put(team.getName(), team.getPlayers().stream().map(Player::getName).collect(Collectors.toList()));
        List<String> playerList = teams.get(0).getPlayers().stream().map(Player::getName).collect(Collectors.toList());
        for (String playerName : playerList)
            System.out.println(playerName);

        teamNameAdapter = new StatsViewExpandableListAdapter(
                this,
                teamNames,
                teamsAndPlayers
        );
        teamListView.setAdapter(teamNameAdapter);
    }

    private void getTeamsFromDatabase() {
        new GetTeamsTask().execute((Void) null);
    }

    void getStatsAndDisplay(int positionOfTeam) {
        Team team = teams.get(positionOfTeam);
        new GetStatsForTeamTask(team).execute((Void) null);
    }

    private void getStatsAndDisplay(Player player) {
        new GetStatsForPlayerTask(player).execute((Void) null);
    }


    public class GetTeamsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                teams = (List<Team>) Database.team.getAll();
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<String> newTeamNames = new ArrayList<>();
            for (Team team : teams) {
                newTeamNames.add(team.getName());
            }
            teamNames = newTeamNames;
            runOnUiThread(() -> {
                renderTeamNames();
            });

            return null;
        }
    }

    private void createDialog(Team team, Stats stats) {
        StatsViewDialog dialog = new StatsViewDialog(this, stats, team);
        dialog.createDialog();
    }

    public class GetStatsForTeamTask extends AsyncTask<Void, Void, Stats> {

        private Team team;

        public GetStatsForTeamTask(Team team) {
            this.team = team;
        }

        @Override
        protected Stats doInBackground(Void... voids) {
            Stats stats = tryGetStatsForTeam();
            return stats;
        }

        private Stats tryGetStatsForTeam() {
            try {
                return Database.stats.getStatsForTeam(team);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Stats stats) {
            createDialog(team, stats);
        }
    }

    private void createDialog(Player player, Stats stats) {
        StatsViewDialog dialog =  new StatsViewDialog(this, stats, player);
        dialog.createDialog();
    }

    public class GetStatsForPlayerTask extends AsyncTask<Void, Void, Stats> {

        private Player player;

        GetStatsForPlayerTask(Player player) {
            this.player = player;
        }

        @Override
        protected Stats doInBackground(Void... voids) {
            Stats stats = tryGetStatsForTeam();
            return stats;
        }

        private Stats tryGetStatsForTeam() {
            try {
                return Database.stats.getStatsForPlayer(player);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Stats stats) {
            createDialog(player, stats);
        }
    }
}

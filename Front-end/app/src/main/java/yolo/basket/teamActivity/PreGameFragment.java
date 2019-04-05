package yolo.basket.teamActivity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yolo.basket.GameActivity;
import yolo.basket.R;
import yolo.basket.db.Database;
import yolo.basket.db.game.Game;
import yolo.basket.db.player.Player;
import yolo.basket.db.team.Team;



/*TODO
Connect to Database => Connect to GameActivity,


 */

public class PreGameFragment extends Fragment {

    private FragmentPreGameListener listener;

    private Button startGame;
    private Team team;

    public PreGameFragment() {
    }

    public interface FragmentPreGameListener {
    }

    private ArrayAdapter<String> startingPlayersAdapter;
    private ArrayAdapter<String> availablePlayersAdapter;

    private String[] arrStartingPlayers = {
            };

    private String[] arrPlayersAvailable = {
            "None"
            };

    private Long teamId;
    private String teamName;

    private ListView availableListView;
    private ListView startingListView;

    private ArrayList<Player> startingPlayers  = new ArrayList<>();
    private ArrayList<Player> availablePlayers = new ArrayList<>();

    public void initLists() {
        availablePlayersAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );

        startingPlayersAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );

        availableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = availablePlayers.get(position);
                startingPlayers.add(player);
                availablePlayers.remove(player);
                updateLists();
            }
        });

        startingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Player player = startingPlayers.get(position);
                availablePlayers.add(player);
                startingPlayers.remove(player);
                updateLists();
            }
        });

        availableListView.setAdapter(availablePlayersAdapter);
        startingListView.setAdapter(startingPlayersAdapter);
    }

    public void updateLists() {
        List<String> startingPlayerNames = new ArrayList<>();
        List<String> availablePlayerNames = new ArrayList<>();
        for (Player player : startingPlayers)
            startingPlayerNames.add(player.getName());
        for (Player player : availablePlayers)
            availablePlayerNames.add(player.getName());

        startingPlayersAdapter.clear();
        startingPlayersAdapter.addAll(startingPlayerNames);
        availablePlayersAdapter.clear();
        availablePlayersAdapter.addAll(availablePlayerNames);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        View view = inflater.inflate(R.layout.pregame, container, false);
        availableListView = view.findViewById(R.id.playersAvailable);
        startingListView = view.findViewById(R.id.playerList);

        Bundle bundle = this.getArguments();
        teamName = "";
        teamId = 1L;
        if (bundle != null) {
            teamId = bundle.getLong("teamId");
            teamName = bundle.getString("teamName");
        }

        System.out.println(teamId +  " -- " + teamName);
        initLists();

        GetOneTeamTask getOneTeamTask = new GetOneTeamTask();
        getOneTeamTask.execute((Void) null);
        return view;
    }

    public class CreateGameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Game game = new Game();
            game.setStartingLineup(startingPlayers);
            game.setBench(availablePlayers);
            game.setTimeOfGame(System.currentTimeMillis());
            game.setStadiumName("Interplanetary stadium of Oli Pals");
            game.setTeamId(teamId);
            try {
                game = (Game) Database.game.save(game);
                Database.user.setActiveGame(game);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(() -> {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
            });

            return (Void) null;
        }
    }

    public class GetOneTeamTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                team = (Team) Database.team.getOne(teamId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            startingPlayers = (ArrayList<Player>) team.getPlayers();
            getActivity().runOnUiThread(() -> {
                updateLists();
            });
            return (Void) null;
        }
    }
}


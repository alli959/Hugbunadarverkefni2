package yolo.basket.teamActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private Long teamId;
    private String teamName;

    private ListView availableListView;
    private ListView startingListView;

    private List<Player> startingPlayers  = new ArrayList<>();
    private List<Player> availablePlayers = new ArrayList<>();

    private Button startGameButton;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        view = inflater.inflate(R.layout.pregame, container, false);
        unBundleArguments();

        retrieveViews();
        bindStartGameButton();
        initLists();

        GetOneTeamTask getOneTeamTask = new GetOneTeamTask();
        getOneTeamTask.execute((Void) null);
        return view;
    }

    private void retrieveViews() {
        availableListView = view.findViewById(R.id.availablePlayers);
        startingListView = view.findViewById(R.id.startingPlayers);
        startGameButton = view.findViewById(R.id.startGame);
    }

    private void unBundleArguments() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        teamId = bundle.getLong("teamId");
        teamName = bundle.getString("teamName");
    }

    private void bindStartGameButton() {
        startGameButton.setOnClickListener(view -> new CreateGameTask().execute((Void) null));
    }

    private void initLists() {
        startingPlayersAdapter  = createList();
        availablePlayersAdapter = createList();

        startingListView.  setOnItemClickListener(moveBetweenListsOnClick(startingPlayers, availablePlayers));
        availableListView. setOnItemClickListener(moveBetweenListsOnClick(availablePlayers, startingPlayers));

        startingListView.  setAdapter(startingPlayersAdapter);
        availableListView. setAdapter(availablePlayersAdapter);
    }

    private ArrayAdapter<String> createList() {
        return new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
    }

    private AdapterView.OnItemClickListener moveBetweenListsOnClick(List<Player> source, List<Player> destination) {
        return (parent, view, position, id) -> {
            destination.add(source.remove(position));
            updateLists();
        };
    }

    public void updateLists() {
        getActivity().runOnUiThread(() -> {
            startingPlayersAdapter.clear();
            startingPlayersAdapter.addAll(getPlayerNames(startingPlayers));
            availablePlayersAdapter.clear();
            availablePlayersAdapter.addAll(getPlayerNames(availablePlayers));
        });
    }

    public List<String> getPlayerNames(List<Player> players) {
        return players
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    private Game createGame() {
        Game game = new Game();
        game.setStartingLineup(startingPlayers);
        game.setBench(availablePlayers);
        game.setTimeOfGame(System.currentTimeMillis());
        game.setStadiumName("Interplanetary stadium of Oli Pals");
        game.setTeamId(teamId);
        return game;
    }

    private void startGameActivity() {
        startActivity(new Intent(getActivity(), GameActivity.class));
    }

    public class CreateGameTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            return trySaveGame();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
                startGameActivity();
        }

        private boolean trySaveGame() {
            try {
                Game game = (Game) Database.game.save(createGame());
                Database.user.setActiveGame(game);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public class GetOneTeamTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return tryGetOneTeam();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                availablePlayers.clear();
                availablePlayers.addAll(team.getPlayers());
                updateLists();
            }
        }

        private boolean tryGetOneTeam() {
            try {
                team = (Team) Database.team.getOne(teamId);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}


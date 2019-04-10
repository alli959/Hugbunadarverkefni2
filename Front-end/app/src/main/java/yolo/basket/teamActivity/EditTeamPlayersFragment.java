package yolo.basket.teamActivity;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import yolo.basket.R;
import yolo.basket.db.Database;
import yolo.basket.db.player.Player;
import yolo.basket.db.team.Team;

@TargetApi(Build.VERSION_CODES.N)
public class EditTeamPlayersFragment extends Fragment {

    private boolean isRightPlayerView = false;
    private FragmentPlayerListener listener;
    private Button createPlayerButton;
    private EditText playerName;
    private EditText playerPosition;
    private EditText playerJerseyNumber;
    private TextView teamNameTextView;
    private Team team;

    public interface FragmentPlayerListener {
        void onPlayerFragmentInput(CharSequence input);
    }

    private ArrayAdapter<String> nameViewAdapter;
    private ArrayAdapter<String> numberViewAdapter;
    private ArrayAdapter<String> positionViewAdapter;

    private String teamName;
    private Long teamId;
    private View view;
    private List<Player> players = new ArrayList<>();

    private ListView listView;

    private String[] arrPlayerNames = {
            "John",
            "Charlie",
            "Prump",};

    private String[] arrPlayerPositions = {
            "Center",
            "PF",
            "PG"
    };

    private String[] arrPlayerJerseyNumbers = {
            "12",
            "1",
            "13"
    };

    private ArrayList<String> playerNames = new ArrayList<>(Arrays.asList(arrPlayerNames));
    private ArrayList<String> playerPositions = new ArrayList<>(Arrays.asList(arrPlayerPositions));
    private ArrayList<String> playerJerseyNumbers = new ArrayList<>(Arrays.asList(arrPlayerJerseyNumbers));

    public void displayPlayers() {

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> positions = new ArrayList<>();
        ArrayList<String> jerseyNumbers = new ArrayList<>();

        for (Player player : players) {
            names.add(player.getName());
            positions.add(player.getPlayerPos());
            jerseyNumbers.add(player.getPlayerNr().toString());
        }

        this.playerNames = names;
        this.playerJerseyNumbers = jerseyNumbers;
        this.playerPositions = positions;

        nameViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                players.stream().map(Player::getName).toArray(String[]::new)
        );

        numberViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                players.stream().map(Player::getPlayerNr).map(l -> String.valueOf(l)).toArray(String[]::new)
        );

        positionViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                players.stream().map(Player::getPlayerPos).toArray(String[]::new)
        );

        teamNameTextView.setText(teamName);

        listView = view.findViewById(R.id.startingPlayers);
        listView.setAdapter(nameViewAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Bundle bundle = this.getArguments();

        view = inflater.inflate(R.layout.edit_players_fragment, container, false);
        createPlayerButton = view.findViewById(R.id.button_addPlayer);
        playerName = view.findViewById(R.id.playerName);
        playerJerseyNumber = view.findViewById(R.id.playerJerseyNumber);
        playerPosition = view.findViewById(R.id.playerPosition);
        teamNameTextView = view.findViewById(R.id.rightPlayerHeader);

        teamId = -1L;
        teamName = "";
        if (bundle != null) {
            teamId = bundle.getLong("teamId");
            teamName = bundle.getString("teamName");
        }
        createPlayerButton.setOnClickListener(v -> {
            CharSequence name = playerName.getText();
            Long playerNumber;
            String playerPos = playerPosition.getText().toString();
            String playerNumberString = playerJerseyNumber.getText().toString();
            try {
                playerNumber = Long.parseLong(playerNumberString);
            } catch(Exception e) {
                playerNumber = 0L;
            }

            Player player = new Player();
            player.setName(name.toString());
            player.setPlayerNr(playerNumber);
            player.setPlayerPos(playerPos);
            player.setTeamId(teamId);
            CreatePlayerTask createPlayerTask = new CreatePlayerTask(player);
            createPlayerTask.execute((Void) null);
        });

        displayPlayers();
        GetOneTeamTask getOneTeamTask = new GetOneTeamTask();
        getOneTeamTask.execute((Void) null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                Log.d("name", name);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentPlayerListener) {
            listener = (FragmentPlayerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TeamSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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

            players = team.getPlayers();
            if (isAdded())
                getActivity().runOnUiThread(() -> {
                    displayPlayers();
                });

            return (Void) null;
        }
    }

    public class CreatePlayerTask extends AsyncTask<Void, Void, Void> {
        Player newPlayer;

        public CreatePlayerTask(Player newPlayer) {
            this.newPlayer = newPlayer;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                newPlayer = (Player) Database.player.save(newPlayer);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(() -> {
                GetOneTeamTask getOneTeamTask = new GetOneTeamTask();
                getOneTeamTask.execute((Void) null);
            });

            return (Void) null;
        }
    }
}




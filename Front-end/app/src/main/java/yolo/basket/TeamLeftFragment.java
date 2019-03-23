package yolo.basket;


import android.content.Context;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yolo.basket.db.Database;
import yolo.basket.db.team.Team;

public class TeamLeftFragment extends Fragment {

    private boolean isRightTeamView = false;
    private boolean isRightPlayerView = false;
    private boolean isStartGameView = false;
    private FragmentLeftListener listener;
    private Button createTeamButton;
    private Button startGameButton;

    private List<Team> teams;

    public interface FragmentLeftListener {
        void showRightTeamView(boolean value);
        void showStartGameView(boolean value);
        void showRightPlayerView(boolean value, Long teamId, String teamName);
    }

    private ArrayAdapter<String> listViewAdapter;

    private String[] arrTeamNames = {
            "Fjölnir",
            "KR",
            "Prumpuliðið",};

    private ArrayList<String> teamNames = new ArrayList<String>(Arrays.asList(arrTeamNames));
    private View view;
    private ListView listView;

    private void displayTeamNames() {

<<<<<<< HEAD

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.team_left_fragment, container, false);
        createTeamButton = view.findViewById(R.id.button_createTeam);

        startGameButton = view.findViewById(R.id.button_startGame);
        ListView listView = (ListView) view.findViewById(R.id.teamList);
=======
        listView = (ListView) view.findViewById(R.id.teamList);
>>>>>>> async
        listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                teamNames
        );

        listView.setAdapter(listViewAdapter);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.team_left_fragment, container, false);
        createTeamButton = view.findViewById(R.id.button_createTeam);
        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRightTeamView = !isRightTeamView;
                isRightPlayerView = false;
                isStartGameView = false;
                listener.showRightTeamView(isRightTeamView);
            }
        });

<<<<<<< HEAD
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartGameView = !isStartGameView;
                isRightTeamView = false;
                isRightPlayerView = false;
                listener.showStartGameView(isStartGameView);

            }
        });
=======
        displayTeamNames();
        updateTeamNames();
>>>>>>> async

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRightTeamView = false;
                isStartGameView = false;
                isRightPlayerView = ! isRightPlayerView;
                listener.showRightPlayerView(isRightPlayerView, teams.get(position).getId(), teams.get(position).getName());
            }
        });

        return view;
    }

    public void updateTeamNames() {
        GetTeamsTask getTeamsTask = new GetTeamsTask();
        getTeamsTask.execute((Void) null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentLeftListener) {
            listener = (FragmentLeftListener) context;
        }

        else {
            throw new RuntimeException(context.toString()
            + " must implement FragmentLeftListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public class GetTeamsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                teams = (List<Team>) Database.team.getAll();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<String> newTeamNames = new ArrayList<>();
            for (Team team : teams) {
                newTeamNames.add(team.getName());
            }

            teamNames = newTeamNames;

            getActivity().runOnUiThread(() -> displayTeamNames());

            return (Void) null;
        }
    }

}

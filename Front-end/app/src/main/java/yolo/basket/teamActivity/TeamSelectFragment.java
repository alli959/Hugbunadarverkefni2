package yolo.basket.teamActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yolo.basket.R;
import yolo.basket.db.Database;
import yolo.basket.db.team.Team;

public class TeamSelectFragment extends Fragment {

    private TeamSelectListener activity;
    private Button createTeamButton;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private static final boolean PREGAME = true;

    private List<Team> teams = new ArrayList<>();
    private boolean radioButtonStatus = false;


    public interface TeamSelectListener {
        void showCreateTeamFragment();
        void onTeamSelected(Long id);
    }

    private ArrayAdapter<String> listViewAdapter;
    private ArrayList<String> teamNames = new ArrayList<>();
    private View view;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        view = inflater.inflate(R.layout.team_select_fragment, container, false);

        retrieveViews();
        displayTeamNames();
        bindCreateTeamButton();
        bindRadioButtons();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            activity.onTeamSelected(teams.get(position).getId());
        });

        return view;
    }

    private void retrieveViews() {
        listView = view.findViewById(R.id.teamList);
        createTeamButton = view.findViewById(R.id.button_createTeam);
        radioGroup = view.findViewById(R.id.radio_group);
    }

    private void displayTeamNames() {
        listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                teamNames
        );
        listView.setAdapter(listViewAdapter);
    }

    private void bindCreateTeamButton() {
        createTeamButton.setOnClickListener(view -> activity.showCreateTeamFragment());
    }

    private void bindRadioButtons() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = radioGroup.findViewById(checkedId);
            radioButtonStatus = radioButton.getText().equals("Game");
            createTeamButton.setVisibility(radioButtonStatus ? View.GONE : View.VISIBLE);
        });
    }

    void updateTeamNames() {
        GetTeamsTask getTeamsTask = new GetTeamsTask();
        getTeamsTask.execute((Void) null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateTeamNames();

        if(context instanceof TeamSelectListener) {
            activity = (TeamSelectListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
            + " must implement TeamSelectListener");
        }
    }

    public boolean isPreGame() {
        return radioButtonStatus == PREGAME;
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
            if (isAdded())
                getActivity().runOnUiThread(() -> displayTeamNames());
            return null;
        }
    }
}

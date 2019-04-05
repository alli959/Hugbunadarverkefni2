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
import android.widget.AdapterView;
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

/*
The fragment where you choose either creation or game by radio buttons.

Case Creation:
    You can either:
        click createTeam witch activates "CreateTeamFragment"
        click on any of your teams and activate "EditTeamPlayersFragment"


Case Game:
    CreateTeam button disappears
    You can:
        Click on any of your teams and activate "PreGameFragment"
 */

public class TeamSelectFragment extends Fragment {

    private boolean isRightTeamView = false;
    private boolean isRightPlayerView = false;
    private FragmentLeftListener listener;
    private Button createTeamButton;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private List<Team> teams;
    private boolean isPregame = false;

    public boolean getIsPregame() {
        return isPregame;
    }

    public interface FragmentLeftListener {
        void showRightTeamView(boolean value);
        void updateRightSideLayout(Long id, String name);
    }

    private ArrayAdapter<String> listViewAdapter;
    private ArrayList<String> teamNames = new ArrayList<>();
    private View view;
    private ListView listView;

    private void displayTeamNames() {
        listView = (ListView) view.findViewById(R.id.teamList);
        listViewAdapter = new ArrayAdapter<>(
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

        /*Check if you should show view*/
        createTeamButton.setOnClickListener(view -> {
            isRightTeamView = !isRightTeamView;
            isRightPlayerView = false;
            listener.showRightTeamView(isRightTeamView);
        });

        radioGroup = view.findViewById(R.id.radio_group);



        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            System.out.println("Radio button click bruh");
            radioButton = radioGroup.findViewById(checkedId);
            String text = (String) radioButton.getText();
            isPregame = text.equals("Game");
            createTeamButton.setVisibility(isPregame ? View.GONE : View.VISIBLE);
        });

        displayTeamNames();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isRightTeamView = false;
                isRightPlayerView = ! isRightPlayerView;
                listener.updateRightSideLayout(teams.get(position).getId(), teams.get(position).getName());
            }
        });

        return view;
    }

    public void changeRightPlayerView(){
        isRightPlayerView = !isRightPlayerView;
    }

    public void updateTeamNames() {
        GetTeamsTask getTeamsTask = new GetTeamsTask();
        getTeamsTask.execute((Void) null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateTeamNames();

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
            if (isAdded())
                getActivity().runOnUiThread(() -> displayTeamNames());
            return (Void) null;
        }
    }
}

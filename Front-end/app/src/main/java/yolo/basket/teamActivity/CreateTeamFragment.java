package yolo.basket.teamActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

import yolo.basket.R;
import yolo.basket.db.Database;
import yolo.basket.db.team.Team;



/*
The fragment where you create teams, the values in this fragment are sent via
interface to TeamActivity.
 */

public class CreateTeamFragment extends Fragment {

    private Team newTeam;

    private CreateTeamListener activity;
    private EditText teamName;
    private Button submitButton;
    private View view;

    public interface CreateTeamListener {
        void onCreateTeam();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_team_fragment, container, false);
        activity = (CreateTeamListener) this.getActivity();
        retrieveViews();
        bindSubmitButton();
        return view;
    }

    private void retrieveViews() {
        teamName = view.findViewById(R.id.team_name);
        submitButton = view.findViewById(R.id.button_addteam);
    }

    private void bindSubmitButton() {
        submitButton.setOnClickListener(v -> {
            CreateTeamTask createTeamTask = new CreateTeamTask();
            createTeamTask.execute((Void) null);

        });
    }

    public class CreateTeamTask extends AsyncTask<Void, Void, Boolean> {

        private Team createTeam() {
            Team team = new Team();
            team.setName(teamName.getText().toString());
            return team;
        }

        private boolean trySaveTeam() {
            try {
                newTeam = (Team) Database.team.save(createTeam());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return trySaveTeam();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            activity.onCreateTeam();
        }
    }

}

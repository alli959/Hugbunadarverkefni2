package yolo.basket;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;

public class TeamLeftFragment extends Fragment {

    private boolean isRightTeamView = false;
    private boolean isRightPlayerView = false;
    private boolean isStartGameView = false;
    private FragmentLeftListener listener;
    private Button createTeamButton;
    private Button startGameButton;

    public interface FragmentLeftListener {
        void showRightTeamView(boolean value);
        void showRightPlayerView(boolean value, CharSequence name);
        void showStartGameView(boolean value);
    }


    private ArrayAdapter<String> listViewAdapter;



    private String[] arrTeamNames = {
            "Fjölnir",
            "KR",
            "Prumpuliðið",};

    private ArrayList<String> teamNames = new ArrayList<String>(Arrays.asList(arrTeamNames));



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.team_left_fragment, container, false);
        createTeamButton = view.findViewById(R.id.button_createTeam);

        startGameButton = view.findViewById(R.id.button_startGame);
        ListView listView = (ListView) view.findViewById(R.id.teamList);
        listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                teamNames
        );

        listView.setAdapter(listViewAdapter);


        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRightTeamView = !isRightTeamView;
                isRightPlayerView = false;
                isStartGameView = false;
                listener.showRightTeamView(isRightTeamView);
            }
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartGameView = !isStartGameView;
                isRightTeamView = false;
                isRightPlayerView = false;
                listener.showStartGameView(isStartGameView);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence name = (CharSequence) parent.getItemAtPosition(position);
                isRightTeamView = false;
                isStartGameView = false;
                isRightPlayerView = ! isRightPlayerView;
                listener.showRightPlayerView(isRightPlayerView, name);
            }
        });

        return view;
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

    public void updateTeamNames(CharSequence text){
        listViewAdapter.add(String.valueOf(text));
    }
}




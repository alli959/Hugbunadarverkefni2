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



/*TODO
Connect to Database => Connect to gameActivity,


 */




public class PreGameFragment extends Fragment {

    private boolean isPreGameView = false;
    private FragmentPreGameListener listener;
    private Button startGame;


    public interface FragmentPreGameListener {
        void showRightTeamView(boolean value);
        void showRightPlayerView(boolean value);
        void getTeamName(CharSequence value);
    }

    private ArrayAdapter<String> startingPlayersAdapter;
    private ArrayAdapter<String> availablePlayersAdapter;


    private String[] arrStartingPlayers = {
            };


    /*

    Test inputs
     */

    private String[] arrPlayersAvailable = {
            "Jónas",
            "Palli",
            "Svenni",
            "Sámur",
            "Blómi",
            "Rósi",};


    private ArrayList<String> startingPlayers = new ArrayList<String>(Arrays.asList(arrStartingPlayers));
    private ArrayList<String> playerAvailable = new ArrayList<String>(Arrays.asList(arrPlayersAvailable));



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.pregame, container, false);

        ListView availableListView =  (ListView) view.findViewById(R.id.playersAvailable);

        ListView startingListView = (ListView) view.findViewById(R.id.playerList);


        /*Create
        A clickable list of avilable players in team, and players that are the starting lineup
        TODO
        Connect to database
         */
        availablePlayersAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                playerAvailable
        );

        startingPlayersAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                startingPlayers
                );

        availableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence name = (CharSequence) parent.getItemAtPosition(position);
                startingPlayersAdapter.add(String.valueOf(name));
                availablePlayersAdapter.remove(String.valueOf(name));

            }
        });



        startingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CharSequence name = (CharSequence) parent.getItemAtPosition(position);
                availablePlayersAdapter.add(String.valueOf(name));
                startingPlayersAdapter.remove(String.valueOf(name));

            }
        });




        availableListView.setAdapter(availablePlayersAdapter);

        startingListView.setAdapter(startingPlayersAdapter);

        return view;
    }
}


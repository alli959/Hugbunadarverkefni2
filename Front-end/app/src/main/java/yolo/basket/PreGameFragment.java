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

    private String[] arrPlayersAvailable = {
            "Jónas",
            "Palli",
            "Svenni",};


    private ArrayList<String> startingPlayers = new ArrayList<String>(Arrays.asList(arrStartingPlayers));
    private ArrayList<String> playerAvailable = new ArrayList<String>(Arrays.asList(arrPlayersAvailable));


}


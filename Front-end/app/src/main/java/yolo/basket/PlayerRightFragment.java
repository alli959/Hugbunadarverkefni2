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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerRightFragment extends Fragment {

    private boolean isRightPlayerView = false;
    private FragmentPlayerListener listener;
    private Button createPlayerButton;
    private EditText playerName;
    private EditText playerPosition;
    private EditText playerJerseyNumber;
    private TextView teamName;

    public interface FragmentPlayerListener {
        void onPlayerFragmentInput(CharSequence input);
    }


    private ArrayAdapter<String> listViewAdapter;



    private String[] arrPlayerNames = {
            "John",
            "Charlie",
            "Prump",};

    private ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList(arrPlayerNames));



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_right_fragment, container, false);
        createPlayerButton = view.findViewById(R.id.button_addPlayer);
        playerName = view.findViewById(R.id.playerName);
        playerJerseyNumber = view.findViewById(R.id.playerJerseyNumber);
        playerPosition = view.findViewById(R.id.playerPosition);

        teamName = (TextView) view.findViewById(R.id.rightPlayerHeader);


        ListView listView = (ListView) view.findViewById(R.id.playerList);
        listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                playerNames
        );

        listView.setAdapter(listViewAdapter);






        createPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence name = playerName.getText();
                listViewAdapter.add(String.valueOf(name));

            }
        });

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

    public void setTeamName(CharSequence name){
        teamName.setText(String.valueOf(name));
    }
}




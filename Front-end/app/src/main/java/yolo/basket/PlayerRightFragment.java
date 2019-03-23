package yolo.basket;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
import java.util.List;
import java.util.stream.Collectors;

import static android.support.constraint.Constraints.TAG;

@TargetApi(Build.VERSION_CODES.N)
public class PlayerRightFragment extends Fragment {

    private boolean isRightPlayerView = false;
    private FragmentPlayerListener listener;
    private Button createPlayerButton;
    private EditText playerName;
    private EditText playerPosition;
    private EditText playerJerseyNumber;
    private TextView teamNameTextView;

    public interface FragmentPlayerListener {
        void onPlayerFragmentInput(CharSequence input);
    }


    private ArrayAdapter<String> nameViewAdapter;

    private ArrayAdapter<String> numberViewAdapter;

    private ArrayAdapter<String> positionViewAdapter;



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

    private ArrayList<String> playerNames = new ArrayList<String>(Arrays.asList(arrPlayerNames));
    private ArrayList<String> playerPositions = new ArrayList<String>(Arrays.asList(arrPlayerPositions));
    private ArrayList<String> playerJerseyNumbers = new ArrayList<String>(Arrays.asList(arrPlayerJerseyNumbers));





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Bundle bundle = this.getArguments();


        View view = inflater.inflate(R.layout.player_right_fragment, container, false);
        createPlayerButton = view.findViewById(R.id.button_addPlayer);
        playerName = view.findViewById(R.id.playerName);
        playerJerseyNumber = view.findViewById(R.id.playerJerseyNumber);
        playerPosition = view.findViewById(R.id.playerPosition);
        teamNameTextView = (TextView) view.findViewById(R.id.rightPlayerHeader);

        String teamName = "";
        if (bundle != null) {
            teamName = bundle.getString("teamName");
        }


        teamNameTextView.setText(teamName);


        ListView listView = (ListView) view.findViewById(R.id.playerList);

        nameViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                playerNames
        );

        numberViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                playerJerseyNumbers
        );


        positionViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                playerPositions
        );


        Log.d("test", positionViewAdapter.getItem(0));



        listView.setAdapter(nameViewAdapter);








        createPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence name = playerName.getText();
                nameViewAdapter.add(String.valueOf(name));

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


}




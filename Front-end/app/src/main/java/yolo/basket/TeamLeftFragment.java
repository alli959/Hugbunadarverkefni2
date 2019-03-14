package yolo.basket;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TeamLeftFragment extends Fragment {

    private ArrayAdapter<String> listViewAdapter;

    private String[] teamNames = {
            "Fjölnir",
            "KR",
            "Prumpuliðið",};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_left_fragment, container, false);

        ListView listView = (ListView) view.findViewById(R.id.teamList);
        listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                teamNames
        );

        listView.setAdapter(listViewAdapter);

        return view;
    }

    public void updateTeamNames(CharSequence text){
        listViewAdapter.add((String)text);
    }
}




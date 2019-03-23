package yolo.basket;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TeamRightFragment extends Fragment {

    private FragmentRightListener listener;
    private EditText teamName;
    private Button addTeamButton;
    public interface FragmentRightListener {
        void onRightFragmentInput(CharSequence input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_right_fragment, container, false);
        teamName = view.findViewById(R.id.team_name);
        addTeamButton = view.findViewById(R.id.button_addteam);

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence input = teamName.getText();
                listener.onRightFragmentInput(input);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentRightListener) {
            listener = (FragmentRightListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
            + " must implement FragmentRightListener");
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
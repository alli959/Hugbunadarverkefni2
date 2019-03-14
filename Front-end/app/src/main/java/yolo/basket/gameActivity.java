package yolo.basket;

import android.content.DialogInterface;
import android.graphics.Point;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class gameActivity extends AppCompatActivity {


    private String selectedPlayer;
    private TextView alertTextView;
    private ImageButton court;

    public void defineButtons(){
        findViewById(R.id.leikmadur1).setOnClickListener(buttonClickListener);
        findViewById(R.id.leikmadur2).setOnClickListener(buttonClickListener);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        alertTextView = (TextView) findViewById(R.id.AlertTextView);
        court = (ImageButton) findViewById(R.id.basketBallCourt);

        court.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(gameActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Action for " + selectedPlayer);
                //builder.setMessage("This is an Alert Dialog Message: " + leikmadur1.getText());

                builder.setItems(new CharSequence[]
                                {"action 1", "action 2", "action 3", "action 4"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                // TODO búa til action föll sem tekur inn id af player og uppfærir rétt action í db.
                                switch (which) {
                                    case 0:
                                        Toast.makeText(gameActivity.this, "action 1", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(gameActivity.this, "action 2", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(gameActivity.this, "action 3", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Toast.makeText(gameActivity.this, "action 4", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                builder.create().show();

            }

        });
    }
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.leikmadur1:
                    //Action
                    Button leikmadur1 = (Button) findViewById(R.id.leikmadur1);
                    selectedPlayer = (String)leikmadur1.getText();
                    break;
                case R.id.leikmadur2:
                    //Action
                    Button leikmadur2 = (Button) findViewById(R.id.leikmadur2);
                    selectedPlayer = (String)leikmadur2.getText();
                    break;
            }
        }
    };

}

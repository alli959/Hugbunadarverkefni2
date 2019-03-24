package yolo.basket.canvas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import yolo.basket.gameActivity;



public class CanvasView extends View {

    public int width;
    private Bitmap mBitmap;
    Context context;
    private float mX;
    private float mY;

    @Override
    public float getX() {
        return mX;
    }

    @Override
    public float getY() {
        return mY;
    }

    private gameActivity game;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void hnit(float x, float y) {
        mX = x;
        mY = y;
        Log.d("110495", String.valueOf(mX));
        Log.d("110495", String.valueOf(mY));
        checkHnit(mX, mY);
    }

    //TODO athuga hnitin til að tjekka hvar leikmaðurinn skaut eða performaði actionið

    private void checkHnit(float x, float y){

        createShotAlert();

    }

    private void createShotAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Næ í leikmanninn sem er valinn, svo þarf líka að sækja id-ið á leikmanninum til þess að
        // uppfæra databaseinn
        game = new gameActivity();
        String player = game.getSelectedPlayer();
        Log.d("110495", "player:"+player);

        if(player == ""){
            return;
        }

        int id = 1;

        builder.setCancelable(true);
        builder.setTitle("Action for " + player);
        //builder.setMessage("This is an Alert Dialog Message: " + leikmadur1.getText());

        builder.setItems(new CharSequence[]
                        {"Made Shot", "Missed Shot", "Committed Foul", "Turnover"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        // TODO búa til action föll sem tekur inn id af player og uppfærir rétt action í db.
                        switch (which) {
                            case 0:
                                Toast.makeText(context, player + " Made Shot", Toast.LENGTH_SHORT).show();
                                madeShotAlert(player, id);
                                break;
                            case 1:
                                Toast.makeText(context, player + " Missed Shot", Toast.LENGTH_SHORT).show();
                                createShotAlert();
                                break;
                            case 2:
                                Toast.makeText(context, player + " Committed Foul", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(context, player + " Turnover", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void madeShotAlert(String player, int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);



        builder.setCancelable(true);
        builder.setTitle("Made Shot");
        //builder.setMessage("This is an Alert Dialog Message: " + leikmadur1.getText());

        builder.setItems(new CharSequence[]
                        {"Made Shot", "Missed Shot", "Committed Foul", "Turnover"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        // TODO búa til action föll sem tekur inn id af player og uppfærir rétt action í db.
                        switch (which) {
                            case 0:
                                Toast.makeText(context, player + " Made Shot", Toast.LENGTH_SHORT).show();
                                createShotAlert();
                                break;
                            case 1:
                                Toast.makeText(context, player + " Missed Shot", Toast.LENGTH_SHORT).show();
                                createShotAlert();
                                break;
                            case 2:
                                Toast.makeText(context, player + " Committed Foul", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(context, player + " Turnover", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        builder.create().show();
    }



    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            hnit(x, y);
            invalidate();
        }

        return true;
    }
}

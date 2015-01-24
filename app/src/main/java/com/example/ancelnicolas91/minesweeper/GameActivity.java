package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class GameActivity extends Activity implements View.OnClickListener, View.OnLongClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // On récupère l'Intent qui a démarré cette Activity
        final Intent intent = getIntent();

        Bundle receiveBundle = intent.getBundleExtra("GameParams");
        Log.d("WESH", "Cols :" + Integer.toString(receiveBundle.getInt("Cols")));
        Log.d("WESH", "Rows :"+Integer.toString(receiveBundle.getInt("Rows")));
        Log.d("WESH", "Mines :"+Integer.toString(receiveBundle.getInt("Mines")));

        int nbrColumns = receiveBundle.getInt("Cols");
        int nbrRows = receiveBundle.getInt("Rows");

        setContentView(R.layout.activity_game);

        LinearLayout layout = (LinearLayout)findViewById(R.id.game_container);


        for(int i = 0; i < nbrRows; i++){
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params);

            LinearLayout.LayoutParams paramsCell = new LinearLayout.LayoutParams(80,80);

            for(int j = 0; j < nbrColumns; j++){
                ImageButton cell = new ImageButton(this);
                cell.setLayoutParams(paramsCell);
                cell.setImageResource(R.drawable.cells_hide);
                cell.setId(j + 1 + (i * nbrColumns));
                cell.setScaleType(ImageView.ScaleType.FIT_XY);
                cell.setPadding(0, 0, 0, 0);
                cell.setOnClickListener(this);
                cell.setOnLongClickListener(this);
                row.addView(cell);
            }
            layout.addView(row);
        }
    }


    public void resetGame(View v){
        Log.d("TESTTTT", "RESET GAME MOTHERFUCKER");
    }

    @Override
    public void onClick(View v) {
        Log.d("TESTTTT", Integer.toString(v.getId()));
    }

    public void longclick(int id){
        Log.d("TESTTTToooo", Integer.toString(id));
    }

    public boolean onLongClick(View v) {

        int id = v.getId();
        ImageButton imgBtn = (ImageButton)findViewById(id);
        imgBtn.setImageResource(R.drawable.cells_flag);


        return true;
    }

}

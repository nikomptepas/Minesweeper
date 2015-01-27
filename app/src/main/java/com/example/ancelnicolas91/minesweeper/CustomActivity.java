package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class CustomActivity extends Activity {

    private int nbrOfCols = 10;
    private int nbrOfRows = 10;
    private int nbrOfMines = 10;


    private SeekBar volumeControl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        if(savedInstanceState != null){

            nbrOfCols = savedInstanceState.getInt("nbrOfCols");
            nbrOfRows = savedInstanceState.getInt("nbrOfRows");
            nbrOfMines = savedInstanceState.getInt("nbrOfMines");

            updateValues((TextView)findViewById(R.id.tv_nbrCols), nbrOfCols);
            updateValues((TextView)findViewById(R.id.tv_nbrRows), nbrOfRows);
            updateValues((TextView)findViewById(R.id.tv_nbrMines), nbrOfMines);


        }



    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("nbrOfCols",nbrOfCols);
        savedInstanceState.putInt("nbrOfRows",nbrOfRows);
        savedInstanceState.putInt("nbrOfMines",nbrOfMines);

        super.onSaveInstanceState(savedInstanceState);
    }



    public void changeColsNbr(View v){
        String operation = v.getTag().toString();
        if(operation.equals("add")){
            if(nbrOfCols<20)
                nbrOfCols++;
        }else{
            if(nbrOfCols>7)
             nbrOfCols--;
        }

        updateValues((TextView)findViewById(R.id.tv_nbrCols), nbrOfCols);
        updateValues((TextView)findViewById(R.id.tv_nbrMines), nbrOfMines);

    }

    public void updateValues(TextView tv, int value){
        tv.setText(Integer.toString(value));
        if(nbrOfMines > (nbrOfRows*nbrOfCols)-1){
            nbrOfMines = (nbrOfRows*nbrOfCols)-1;
            updateValues((TextView)findViewById(R.id.tv_nbrMines), nbrOfMines);
        }
    }

    public void changeRowsNbr(View v){
        String operation = v.getTag().toString();
        if(operation.equals("add")){
            if(nbrOfRows<20)
                nbrOfRows++;
        }else{
            if(nbrOfRows>=3)
                nbrOfRows--;
        }

        updateValues((TextView)findViewById(R.id.tv_nbrRows), nbrOfRows);

    }

    public void changeMinesNbr(View v){
        String operation = v.getTag().toString();
        if(operation.equals("add")){
            if(nbrOfMines<(nbrOfCols*nbrOfRows)-1)
                nbrOfMines++;
        }else{
            if(nbrOfMines>1)
                nbrOfMines--;
        }

        updateValues((TextView)findViewById(R.id.tv_nbrMines), nbrOfMines);

    }

    public void playGameWithParams(View v){

        Bundle sendBundle = new Bundle();

        sendBundle.putInt("Rows", nbrOfRows);
        sendBundle.putInt("Cols", nbrOfCols);
        sendBundle.putInt("Mines", nbrOfMines);

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("GameParams",sendBundle);

        startActivity(intent);
    }




}

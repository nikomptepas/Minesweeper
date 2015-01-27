package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LevelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

    }

    public void playGameWithParams(View v){

        Bundle sendBundle = new Bundle();

        switch (v.getId()) {
            case R.id.btn_easy:
                sendBundle.putInt("Rows", 7);
                sendBundle.putInt("Cols", 7);
                sendBundle.putInt("Mines", 7);
                break;
            case R.id.btn_normal:
                sendBundle.putInt("Rows", 11);
                sendBundle.putInt("Cols", 11);
                sendBundle.putInt("Mines", 30);
                break;
            case R.id.btn_hard:
                sendBundle.putInt("Rows", 16);
                sendBundle.putInt("Cols", 16);
                sendBundle.putInt("Mines", 60);
                break;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("GameParams",sendBundle);

        startActivity(intent);
    }

    public void goToCustomParameters(View v){
        Intent intent = new Intent(this, CustomActivity.class);
        startActivity(intent);
    }

}

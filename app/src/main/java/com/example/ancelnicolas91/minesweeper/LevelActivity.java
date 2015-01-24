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
                sendBundle.putInt("Rows", 9);
                sendBundle.putInt("Cols", 9);
                sendBundle.putInt("Mines", 10);
                break;
            case R.id.btn_normal:
                sendBundle.putInt("Rows", 16);
                sendBundle.putInt("Cols", 16);
                sendBundle.putInt("Mines", 40);
                break;
            case R.id.btn_hard:
                sendBundle.putInt("Rows", 21);
                sendBundle.putInt("Cols", 21);
                sendBundle.putInt("Mines", 90);
                break;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("GameParams",sendBundle);

        startActivity(intent);
    }

}

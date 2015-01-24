package com.example.ancelnicolas91.minesweeper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends android.app.Activity {

    private Button btn_play = null;
    private Button btn_scores = null;
    private Button btn_rules = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_rules = (Button)findViewById(R.id.btn_rules);

        btn_rules.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), RulesActivity.class);
                startActivity(intent);
            }
        });

        btn_play = (Button)findViewById(R.id.btn_play);

        btn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
                startActivity(intent);
            }
        });




    }

}

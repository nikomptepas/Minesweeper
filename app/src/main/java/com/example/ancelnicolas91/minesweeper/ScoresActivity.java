package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justine on 18/01/2015.
 */
public class ScoresActivity extends android.app.Activity {

    private ScoresAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //Création d'une instance de ma classe SCORESBDD
        ScoresBDD scoresBDD = new ScoresBDD(this);

        //Création d'un SCORE
        Scores score1 = new Scores("JUUU", 20, 30, "05:33", Boolean.TRUE);
        Scores score2 = new Scores("TEST", 24, 24, "10:83", Boolean.FALSE);

        //On ouvre la base de données pour écrire dedans
        scoresBDD.open();
        //On insère le score que l'on vient de créer
        scoresBDD.insertScore(score1);
        scoresBDD.insertScore(score2);

        // On récupère tout les scores
        Cursor cursor = scoresBDD.getAllScores();

        // On définit les colonnes a récuperer
        String[] columns = new String[] {
                ScoresBDD.COL_PSEUDO,
                ScoresBDD.COL_CASES,
                ScoresBDD.COL_MINES,
                ScoresBDD.COL_TIME,
                ScoresBDD.COL_WIN
        };

        // On définit les layouts a remplir
        int[] to = new int[] {
                R.id.textView_pseudo,
                R.id.textView_cases,
                R.id.textView_mines,
                R.id.textView_time,
                R.id.textView_win,

        };

        SimpleCursorAdapter dataAdapter;

        // Adaptateur pour matcher entre la BDD et la listview
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.scores_info,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);

        // Fermeture de la BDD
        scoresBDD.close();

        View v = listView.getChildAt(0);
        // if your row base layout is LinearLayout
        // get it and find in this layout your TextView by id

        if(v instanceof LinearLayout){
            TextView textView_Win = (TextView) v.findViewById(R.id.textView_win);
            ImageView imgViewYes = (ImageView) v.findViewById(R.id.imgView_yes);
            ImageView imgViewNo = (ImageView) v.findViewById(R.id.imgView_no);

            if (textView_Win.getText().toString() == "0")
            {
                imgViewYes.setVisibility(View.INVISIBLE);
            }
            else
            {
                imgViewNo.setVisibility(View.INVISIBLE);
            }
        }





        //textViewWin.setVisibility(View.INVISIBLE);




    }
}

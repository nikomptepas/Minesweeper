package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

        //Création d'une instance de ma classe LivresBDD
        ScoresBDD scoresBDD = new ScoresBDD(this);

        //Création d'un livre
        Scores score = new Scores("JUUU", 20, 30, "05:33", Boolean.TRUE);

        //On ouvre la base de données pour écrire dedans
        scoresBDD.open();
        //On insère le livre que l'on vient de créer
        scoresBDD.insertScore(score);

        //List<String> myScores = new ArrayList<String>();
        //myScores = scoresBDD.getAllScores();

        ListView listViewScores = (ListView) findViewById(R.id.listView_scores);


       /*
        ListView listViewScores = (ListView) findViewById(R.id.listView_scores);





        String[] values = new String[] { "Device",
                "Géo localisation", "Accéléromètre",
                "Navigateur internet", "Dialogues", "Album photos",
                "Connexion réseau", "Gestion des fichiers",
                "Carnet de contacts" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);


        listViewScores.setAdapter(adapter);

        */
       /* List<Scores> myListScores = new ArrayList<Scores>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myScores);

        listViewScores.setAdapter(adapter);*/

        Cursor cursor = scoresBDD.getAllScores();

        // The desired columns to be bound
        String[] columns = new String[] {
                ScoresBDD.COL_ID,
                ScoresBDD.COL_PSEUDO,
                ScoresBDD.COL_CASES,
                ScoresBDD.COL_MINES,
                ScoresBDD.COL_TIME,
                ScoresBDD.COL_WIN
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.textView_id,
                R.id.textView_pseudo,
                R.id.textView_cases,
                R.id.textView_mines,
                R.id.textView_time,
                R.id.textView_win
        };

        SimpleCursorAdapter dataAdapter;

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.activity_scores,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        scoresBDD.close();
    }
}

package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Justine on 18/01/2015.
 */
public class ScoresActivity extends android.app.Activity {

    private ScoresAdapter dbHelper;
    private ScoresBDD scoresBDD;
    private SimpleCursorAdapter dataAdapter;
    ListView listView;
    long lastID;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        lastID = 0;

        //Création d'une instance de ma classe SCORESBDD
        scoresBDD = new ScoresBDD(this);

        //On ouvre la base de données pour écrire dedans
        scoresBDD.open();

        // On récupère tout les scores
        Cursor cursor = scoresBDD.getAllScores();

        // On définit les colonnes a récuperer
        String[] columns = new String[]{
                ScoresBDD.COL_PSEUDO,
                ScoresBDD.COL_CASES,
                ScoresBDD.COL_MINES,
                ScoresBDD.COL_TIME,
                ScoresBDD.COL_WIN
        };

        // On définit les layouts a remplir
        int[] to = new int[]{
                R.id.textView_pseudo,
                R.id.textView_cases,
                R.id.textView_mines,
                R.id.textView_time,
                R.id.textView_win,

        };


        // Adaptateur pour matcher entre la BDD et la listview
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.scores_info,
                cursor,
                columns,
                to,
                0);

        listView = (ListView) findViewById(R.id.listView1);
        inputSearch = (EditText) findViewById(R.id.myFilter);
        listView.setAdapter(dataAdapter);
        registerForContextMenu(listView);

        // Capture Text in EditText
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                dataAdapter.swapCursor(scoresBDD.getScoreByPseudo(text));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openContextMenu(view);
            }
        });

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_scores, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo infoMenu =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.editItem:
                lastID = infoMenu.id;
                showEditPopup();
                return true;
            case R.id.deleteItem:
                // On supprime de la BDD le score selectionné
                scoresBDD.removeScoreWithID(infoMenu.id);

                // On refresh la listView
                dataAdapter.swapCursor(scoresBDD.getAllScores());

                Toast.makeText(this, "DELETE",
                        Toast.LENGTH_SHORT).show();


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showEditPopup()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.editscores_info, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Pseudo");
        alert.setMessage("Enter new pseudo :");
        alert.setView(textEntryView);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // On récupère ce qui a été saisie par l'utilisateur
                EditText mUserText;
                mUserText = (EditText) textEntryView.findViewById(R.id.newPseudo);
                String newPseudo = mUserText.getText().toString();

                // On modifie le pseudo dans la BDD
                scoresBDD.updatePseudoById(lastID,newPseudo);

                // On rafrachit la listView
                dataAdapter.swapCursor(scoresBDD.getAllScores());

                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                return;
            }
        });
        alert.show();
    }
}

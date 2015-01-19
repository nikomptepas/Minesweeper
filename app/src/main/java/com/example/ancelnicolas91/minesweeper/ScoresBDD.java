package com.example.ancelnicolas91.minesweeper;

/**
 * Created by Justine on 18/01/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ScoresBDD {

    private static final int VERSION_BDD = 1;
    private static final String NAME_BDD = "scores.db";

    public static final String TABLE_SCORES = "table_scores";
    public static final String COL_ID = "ID";
    public static final int NUM_COL_ID = 0;
    public static final String COL_PSEUDO = "Pseudo";
    public static final int NUM_COL_PSEUDO = 1;
    public static final String COL_CASES = "Cases";
    public static final int NUM_COL_CASES = 2;
    public static final String COL_MINES = "Mines";
    public static final int NUM_COL_MINES = 3;
    public static final String COL_TIME = "Time";
    public static final int NUM_COL_TIME = 4;
    public static final String COL_WIN = "Win";
    public static final int NUM_COL_WIN = 5;

    private SQLiteDatabase bdd;

    private BDDSQLite myBDDSQLite;

    public ScoresBDD(Context context){
        //On créer la BDD et sa table
        myBDDSQLite = new BDDSQLite(context, NAME_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = myBDDSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insertScore(Scores scores){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_PSEUDO, scores.getPseudo());
        values.put(COL_CASES, scores.getCases());
        values.put(COL_MINES, scores.getMines());
        values.put(COL_TIME, scores.getTime());
        values.put(COL_WIN, scores.getWin());

        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_SCORES, null, values);
    }

    public int updateScore(int id, Scores scores){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_PSEUDO, scores.getPseudo());
        values.put(COL_CASES, scores.getCases());
        values.put(COL_MINES, scores.getMines());
        values.put(COL_TIME, scores.getTime());
        values.put(COL_WIN, scores.getWin());
        return bdd.update(TABLE_SCORES, values, COL_ID + " = " +id, null);
    }

    public int removeScoreWithID(int id){
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_SCORES, COL_ID + " = " +id, null);
    }

    //Cette méthode permet de convertir un cursor en un score
    private Scores cursorToScore(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Scores scores = new Scores();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        scores.setId(c.getInt(NUM_COL_ID));
        scores.setPseudo(c.getString(NUM_COL_PSEUDO));
        scores.setCases(c.getInt(NUM_COL_CASES));
        scores.setMines(c.getInt(NUM_COL_MINES));
        scores.setTime(c.getString(NUM_COL_TIME));
        scores.setWin(c.getInt(NUM_COL_WIN) > 0);

        //On ferme le cursor
        c.close();

        //On retourne le livre
        return scores;
    }

    //Cette méthode permet de convertir un cursor en un score
    public Cursor getAllScores(){
        Cursor mCursor = bdd.query(TABLE_SCORES, new String[] {COL_ID,
                        COL_PSEUDO, COL_CASES, COL_MINES, COL_TIME, COL_WIN},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}

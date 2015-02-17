package com.example.ancelnicolas91.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ScoresBDD {

    private static final int VERSION_BDD = 2;
    private static final String NAME_BDD = "scores.db";

    public static final String KEY_ROWID = "_id";
    public static final String TABLE_SCORES = "table_scores";
    public static final String _ID = "_id";
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

    public ScoresBDD(Context context)
    {
        //On créer la BDD et sa table
        myBDDSQLite = new BDDSQLite(context, NAME_BDD, null, VERSION_BDD);
    }

    public void open()
    {
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
        return bdd.update(TABLE_SCORES, values, _ID + " = " +id, null);
    }

    public void removeScoreWithID(long id){
        //Suppression d'un livre de la BDD grâce à l'ID
        bdd.delete(TABLE_SCORES, _ID + " = " +id, null);
    }

    public void updatePseudoById(long id, String newPseudo) {
        Cursor c =  bdd.rawQuery("SELECT _id, Pseudo, Cases, Mines, Time, Win FROM table_scores where _id="+ id +";", null);
        c.moveToFirst();
        Scores tmpScore = new Scores();

        tmpScore.setId(c.getInt(NUM_COL_ID));
        tmpScore.setPseudo(newPseudo);
        tmpScore.setCases(c.getInt(NUM_COL_CASES));
        tmpScore.setMines(c.getInt(NUM_COL_MINES));
        tmpScore.setTime(c.getString(NUM_COL_TIME));
        tmpScore.setWin(c.getString(NUM_COL_WIN));
        updateScore(c.getInt(NUM_COL_ID),tmpScore);
    }

    //Cette méthode permet de convertir un cursor en un score
    public Cursor getAllScores(){
        Cursor mCursor = bdd.query(TABLE_SCORES, new String[] {KEY_ROWID,
                        COL_PSEUDO, COL_CASES, COL_MINES, COL_TIME, COL_WIN},
                null, null, null, null, KEY_ROWID + " DESC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //Cette méthode permet de convertir un cursor en un score
    public Cursor getScoreByPseudo(String pseudo){
        Cursor mCursor = bdd.query(TABLE_SCORES, new String[] {KEY_ROWID,
                        COL_PSEUDO, COL_CASES, COL_MINES, COL_TIME, COL_WIN},
               COL_PSEUDO + " LIKE '%" + pseudo + "%'", null, null, null, KEY_ROWID + " DESC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}

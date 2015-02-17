package com.example.ancelnicolas91.minesweeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BDDSQLite extends SQLiteOpenHelper {

    private static final String TABLE_SCORES = "table_scores";
    public static final String KEY_ROWID = "_id";
    private static final String COL_ID = "_id";
    private static final String COL_PSEUDO = "Pseudo";
    private static final String COL_CASES = "Cases";
    private static final String COL_MINES = "Mines";
    private static final String COL_TIME = "Time";
    private static final String COL_WIN = "Win";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_SCORES + " ("
            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PSEUDO + " TEXT NOT NULL, "
            + COL_CASES + " INTEGER NOT NULL, " + COL_MINES + " INTEGER NOT NULL,"+ COL_TIME +
            " TEXT NOT NULL,"+ COL_WIN + " BOOLEAN NOT NULL);";

    public BDDSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on créé la table à partir de la requête écrite dans la variable CREATE_BDD
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_SCORES + ";");
        onCreate(db);
    }

}
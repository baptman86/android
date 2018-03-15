package com.example.baptiste.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Baptiste on 11/03/2018.
 */

public class MaBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_MABASE = "table_mabase";
    private static final String COLONNE_ID = "id";
    private static final String COLONNE_NOM = "nom";
    private static final String COLONNE_PRENOM = "prenom";

    private static final String REQUETE_CREATION_BD = "CREATE TABLE "+ TABLE_MABASE + "("+ COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLONNE_NOM + " TEXT NOT NULL, " + COLONNE_PRENOM + " TEXT NOT NULL);";

    public MaBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(REQUETE_CREATION_BD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_MABASE + ";");
        onCreate(sqLiteDatabase);
    }

}

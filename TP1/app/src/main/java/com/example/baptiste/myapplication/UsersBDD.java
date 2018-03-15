package com.example.baptiste.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.baptiste.myapplication.MaBaseSQLite;

import java.util.ArrayList;
import java.util.List;

public class UsersBDD {

    private static final int BASE_VERSION = 1;
    private static final String BASE_NOM = "mabase.db";
    private static final String TABLE_MABASE = "table_mabase";
    private static final String COLONNE_ID = "id";
    private static final int COLONNE_ID_ID = 0;
    private static final String COLONNE_NOM = "nom";
    private static final int COLONNE_NOM_ID = 1;
    private static final String COLONNE_PRENOM = "prenom";
    private static final int COLONNE_PRENOM_ID = 2;

    private SQLiteDatabase bdd;
    private MaBaseSQLite maBaseSQLite;

    public UsersBDD(Context ctx){
        maBaseSQLite = new MaBaseSQLite(ctx,BASE_NOM,null,BASE_VERSION);
    }

    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBdd() {
        return bdd;
    }


    public List<User> getUsers(){
        Cursor c = bdd.query(
                TABLE_MABASE,
                new String[] {
                        COLONNE_ID,
                        COLONNE_NOM,
                        COLONNE_PRENOM
                },
                null,null,null,null,null);
        List<User> users = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            users.add(cursorToUser(c));
            c.moveToNext();
        }
        return users;
    }

    public long addUser(User user){
        ContentValues values = new ContentValues();
        values.put(COLONNE_NOM,user.getName());
        values.put(COLONNE_PRENOM,user.getSurname());
        return bdd.insert(TABLE_MABASE,null,values);
    }

    public long updateUser(int id, User user){
        ContentValues values = new ContentValues();
        values.put(COLONNE_NOM,user.getName());
        values.put(COLONNE_PRENOM,user.getSurname() );
        return bdd.update(TABLE_MABASE,values,COLONNE_ID + " = " + id,null);
    }

    public long removeUser(int id){
        return bdd.delete(TABLE_MABASE,COLONNE_ID + " = " + id,null);
    }

    public User getUserWithName(String name){
        Cursor c = bdd.query(TABLE_MABASE, new String[] {COLONNE_ID,COLONNE_NOM,COLONNE_PRENOM}, COLONNE_NOM + " LIKE \"" + name +"\"", null, null, null, null);
        c.moveToFirst();
        return cursorToUser(c);
    }

    private User cursorToUser(Cursor c){
        if(c.getCount() <= 0){
            return null;
        }
        else{
            User user = new User();
            user.setId(c.getInt(COLONNE_ID_ID));
            user.setName(c.getString(COLONNE_NOM_ID));
            user.setSurname(c.getString(COLONNE_PRENOM_ID));
            return user;
        }
    }
}

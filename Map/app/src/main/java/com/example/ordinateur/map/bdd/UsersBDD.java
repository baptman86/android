package com.example.ordinateur.map.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ordinateur.map.object.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Baptiste on 11/03/2018.
 */

public class UsersBDD extends SQLiteOpenHelper {

    private static final int BASE_VERSION = 2;
    private static final String BASE_NOM = "mabase.db";
    private static final String TABLE_MABASE = "table_mabase";
    private static final String COLONNE_ID = "id";
    private static final int COLONNE_ID_ID = 0;
    private static final String COLONNE_IDENTIFIANT = "identifiant";
    private static final int COLONNE_IDENTIFIANT_ID = 1;
    private static final String COLONNE_PASSWORD = "mot_de_passe";
    private static final int COLONNE_PASSWORD_ID = 2;
    private static final String COLONNE_SALT = "salt";
    private static final int COLONNE_SALT_ID = 3;
    private static final String COLONNE_NOM = "nom";
    private static final int COLONNE_NOM_ID = 4;
    private static final String COLONNE_PRENOM = "prenom";
    private static final int COLONNE_PRENOM_ID = 5;
    private static final String COLONNE_EMAIL = "email";
    private static final int COLONNE_EMAIL_ID = 6;

    private SQLiteDatabase bdd;
    private static final int SALT_LENGHT = 12;

    private static final String REQUETE_CREATION_BD = "CREATE TABLE "+ TABLE_MABASE + "("+
            COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLONNE_IDENTIFIANT + " TEXT NOT NULL, " +
            COLONNE_PASSWORD + " TEXT NOT NULL, " +
            COLONNE_SALT + " TEXT NOT NULL, " +
            COLONNE_NOM + " TEXT, " +
            COLONNE_PRENOM + " TEXT, " +
            COLONNE_EMAIL + " TEXT);";

    public UsersBDD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UsersBDD(Context ctx){
        super(ctx,BASE_NOM,null,BASE_VERSION);
    }

    public void open(){
        bdd = this.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBdd() {
        return bdd;
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

    //requetes

    public List<User> getUsers(){
        Cursor c = bdd.query(
                TABLE_MABASE,
                new String[] {
                        COLONNE_ID,
                        COLONNE_IDENTIFIANT,
                        COLONNE_PASSWORD,
                        COLONNE_SALT,
                        COLONNE_NOM,
                        COLONNE_PRENOM,
                        COLONNE_EMAIL
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

    public long addUser(String identifiant, String password, String name, String surname, String email){
        String salt = generateSalt();
        ContentValues values = new ContentValues();
        values.put(COLONNE_IDENTIFIANT,identifiant);
        values.put(COLONNE_PASSWORD,encrypt(password,salt));
        values.put(COLONNE_SALT,salt);
        values.put(COLONNE_NOM,name);
        values.put(COLONNE_PRENOM,surname);
        values.put(COLONNE_EMAIL,email);
        return bdd.insert(TABLE_MABASE,null,values);
    }

    public long updateUser(int id, User user){
        ContentValues values = new ContentValues();
        values.put(COLONNE_IDENTIFIANT,user.getIdentifiant());
        values.put(COLONNE_PASSWORD,user.getMot_de_passe());
        values.put(COLONNE_SALT,user.getSalt());
        values.put(COLONNE_NOM,user.getName());
        values.put(COLONNE_PRENOM,user.getSurname());
        values.put(COLONNE_EMAIL,user.getEmail());
        return bdd.update(TABLE_MABASE,values,COLONNE_ID + " = " + id,null);
    }

    public long removeUser(int id){
        return bdd.delete(TABLE_MABASE,COLONNE_ID + " = " + id,null);
    }

    public User getUserWithLogin(String identifiant) {
        Cursor c = bdd.query(TABLE_MABASE, new String[]{COLONNE_ID, COLONNE_IDENTIFIANT, COLONNE_PASSWORD, COLONNE_SALT, COLONNE_NOM, COLONNE_PRENOM, COLONNE_EMAIL}, COLONNE_IDENTIFIANT + " LIKE \"" + identifiant + "\"", null, null, null, null);
        c.moveToFirst();
        return cursorToUser(c);
    }

    //utils

    private User cursorToUser(Cursor c){
        if(c.getCount() <= 0){
            return null;
        }
        else{
            User user = new User();
            user.setId(c.getInt(COLONNE_ID_ID));
            user.setIdentifiant(c.getString(COLONNE_IDENTIFIANT_ID));
            user.setMot_de_passe(c.getString(COLONNE_PASSWORD_ID));
            user.setSalt(c.getString(COLONNE_SALT_ID));
            user.setName(c.getString(COLONNE_NOM_ID));
            user.setSurname(c.getString(COLONNE_PRENOM_ID));
            user.setEmail(c.getString(COLONNE_EMAIL_ID));
            return user;
        }
    }

    public static Boolean testPassword(User user,String password){
        return encrypt(password,user.getSalt()).equals(user.getMot_de_passe());
    }

    //encrypt EXTREMEMENT naif

    public static String encrypt(String Data, String salt){
        String tmp = Data+salt;
        return Integer.toString(tmp.hashCode());
    }

    private static String generateSalt(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(SALT_LENGHT);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}

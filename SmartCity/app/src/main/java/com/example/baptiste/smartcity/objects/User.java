package com.example.baptiste.smartcity.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class User {
    private String identifiant;
    private String mot_de_passe;
    private String salt;
    private String name;
    private String surname;
    private String email;
    private ArrayList<String> conversations_id;

    private static final int SALT_LENGHT = 12;

    public User(){};

    public User(String identifiant, String mot_de_passe,String salt, String name, String surname, String email) {
        this.identifiant = identifiant;
        this.mot_de_passe = mot_de_passe;
        this.salt = salt;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
            this.email = email;
        }

    public void addConversation(String conv_id){
        this.conversations_id.add(conv_id);
    }

    public void removeConversation(Conversation conv){
        this.conversations_id.remove(conv);
    }

    public static Boolean testPassword(User user,String password){
        return encrypt(password,user.getSalt()).equals(user.getMot_de_passe());
    }

    //encrypt EXTREMEMENT naif

    public static String encrypt(String Data, String salt){
        String tmp = Data+salt;
        return Integer.toString(tmp.hashCode());
    }

    public static String generateSalt(){
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

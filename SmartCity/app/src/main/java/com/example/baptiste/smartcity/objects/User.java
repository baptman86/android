package com.example.baptiste.smartcity.objects;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private String login;
    private String password;
    private String salt;
    private String name;
    private String surname;
    private String email;
    private ArrayList<String> conversations_id;

    private static final int SALT_LENGHT = 12;

    public User(){};

    public User(String login, String password, String salt, String name, String surname, String email) {
        this.login = login;
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return encrypt(password,user.getSalt()).equals(user.getPassword());
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

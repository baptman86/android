package com.example.ordinateur.map.object;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String identifiant;
    private String mot_de_passe;
    private String salt;
    private String name;
    private String surname;
    private String email;
    private String domaines; //String[] split by ";"

    public User(){};

    public User(String identifiant, String mot_de_passe,String salt, String name, String surname, String email) {
        this.identifiant = identifiant;
        this.mot_de_passe = mot_de_passe;
        this.salt = salt;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    //parcel part
    public User(Parcel in){
        this.id = in.readInt();
        this.identifiant= in.readString();
        this.mot_de_passe= in.readString();
        this.salt= in.readString();
        this.name= in.readString();
        this.surname= in.readString();
        this.email= in.readString();
        this.domaines = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.identifiant);
        parcel.writeString(this.mot_de_passe);
        parcel.writeString(this.salt);
        parcel.writeString(this.name);
        parcel.writeString(this.surname);
        parcel.writeString(this.email);
        parcel.writeString(this.domaines);
    }

    public static final Creator<User> CREATOR= new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);  //using parcelable constructor
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

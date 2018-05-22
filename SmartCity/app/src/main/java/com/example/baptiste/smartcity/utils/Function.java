package com.example.baptiste.smartcity.utils;

import android.content.Context;
import android.content.Intent;

import com.example.baptiste.smartcity.R;
import com.example.baptiste.smartcity.activities.MainActivity;
import com.example.baptiste.smartcity.objects.User;

import java.util.regex.Pattern;


public class Function {

    public static final int LOGIN_MINIMUM_LENGTH = 4;
    public static final int PASSWORD_MINIMUM_LENGTH = 4;

    public static Boolean isValidLogin(Context ctx, String user_login){
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(user_login).find();

        boolean isTooShort = user_login.length() < ctx.getResources().getInteger(R.integer.LOGIN_MINIMUM_LENGTH);

        return !hasSpecialChar && !isTooShort;
    }

    public static Boolean isValidPassword(Context ctx, String user_password){
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(user_password).find();

        boolean isTooShort = user_password.length() < ctx.getResources().getInteger(R.integer.PASSWORD_MINIMUM_LENGTH);

        return !hasSpecialChar && !isTooShort;
    }
}

package com.example.musicstorehn.utils;

// File: app/src/main/java/com/uth/musicstorehn/utils/SessionManager.java

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveLoginData(int userId, String name, String email, String token, String image) {
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USER_NAME, name);
        editor.putString(Constants.KEY_USER_EMAIL, email);
        editor.putString(Constants.KEY_AUTH_TOKEN, token);
        editor.putString(Constants.KEY_USER_IMAGE, image);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void updateProfileImage(String imageUrl) {
        editor.putString(Constants.KEY_USER_IMAGE, imageUrl);
        editor.apply();
    }

    public void updateProfileName(String name) {
        editor.putString(Constants.KEY_USER_NAME, name);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(Constants.KEY_USER_ID, -1);
    }

    public String getUserName() {
        return prefs.getString(Constants.KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return prefs.getString(Constants.KEY_USER_EMAIL, "");
    }

    public String getAuthToken() {
        return "Bearer " + prefs.getString(Constants.KEY_AUTH_TOKEN, "");
    }

    public String getUserImage() {
        return prefs.getString(Constants.KEY_USER_IMAGE, "");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}

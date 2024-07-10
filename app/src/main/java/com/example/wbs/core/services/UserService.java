package com.example.wbs.core.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.utils.constants.Constants;

public class UserService {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public void initSharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(SharedUserModel sharedUserModel) {
        saveInt(Constants.KEY_PREF_USER_ID, sharedUserModel.getUser_id());
                saveString(Constants.KEY_PREF_USERNAME, sharedUserModel.getUsername());
        saveString(Constants.KEY_PREF_ROLE, sharedUserModel.getRole());
                saveString(Constants.KEY_PREF_NAMA_LENGKAP, sharedUserModel.getNama());
                saveBool(Constants.KEY_PREF_IS_LOGIN, sharedUserModel.isLogin());

    }

    public SharedUserModel getUserInfo() {
        SharedUserModel sharedUserModel = new SharedUserModel(
                sharedPreferences.getInt(Constants.KEY_PREF_USER_ID, 0),
                sharedPreferences.getString(Constants.KEY_PREF_USERNAME, ""),
                sharedPreferences.getString(Constants.KEY_PREF_ROLE, ""),
                sharedPreferences.getString(Constants.KEY_PREF_NAMA_LENGKAP, ""),
                sharedPreferences.getBoolean(Constants.KEY_PREF_IS_LOGIN, false)
        );

        return sharedUserModel;

    }
    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void saveBool(String key, boolean val) {
        editor.putBoolean(key, val);
        editor.apply();
    }
    public void logOut() {
        editor.clear();
        editor.apply();
    }
}

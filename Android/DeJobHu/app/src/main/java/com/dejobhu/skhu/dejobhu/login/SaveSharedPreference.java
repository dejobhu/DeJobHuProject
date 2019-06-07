package com.dejobhu.skhu.dejobhu.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    static final String PREF_USER_EMAIL = "email";

    static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

//    계정 정보 저장
    public static void setUserEmail(Context ctx, String userEmail){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.commit();
    }

//    저장된 정보 가져오기
    public static String getUserEmail(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static void clearUserEmail(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}

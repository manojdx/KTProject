package com.miniproj.activity.Utils;
import android.content.Context;
import android.content.SharedPreferences;
public class PreferenceManager {
    private static final String SHARED_PREFS = "App_Sh_Prefs";
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferences mSharedPrefs;
    private static PreferenceManager sInstance;

    public static synchronized PreferenceManager getInstance() {
        PreferenceManager preferenceManager;
        synchronized (PreferenceManager.class) {
            if (sInstance == null) {
                sInstance = new PreferenceManager();
            }
            preferenceManager = sInstance;
        }
        return preferenceManager;
    }

    public static void init(Context ctx) {
        mSharedPrefs = ctx.getSharedPreferences(SHARED_PREFS, 0);
        mEditor = mSharedPrefs.edit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPrefs.getInt(key, defValue);
    }

    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }
    public void setLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }
    public String getString(String key, String defString) {
        return mSharedPrefs.getString(key, defString);
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public boolean getBoolean(String key, boolean defvalue) {
        return mSharedPrefs.getBoolean(key, defvalue);
    }
    public long getLong(String key, long defvalue) {
        return mSharedPrefs.getLong(key, defvalue);
    }

    public void performLogout() {

    }
}

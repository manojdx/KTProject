package com.miniproj.activity.Utils;


import com.miniproj.activity.interfaces.Preference_Keys;

public class Preference_Details {
    private static final PreferenceManager mPrefsMgr = PreferenceManager.getInstance();



    public static Boolean getLoginStatus() {
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.LoginStatus,false);
    }

    public static String getSID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.SID,"");
    }

    public static String getEmail(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.EmailID,"");
    }

    public static String getDisplayName(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.displayName,"");
    }


}

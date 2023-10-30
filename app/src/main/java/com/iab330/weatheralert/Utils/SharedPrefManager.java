package com.iab330.weatheralert.Utils;


import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences getSharedPreference(){

        if (sharedPreferences == null){
            sharedPreferences = MyApp.getPreferences();
        }
        return sharedPreferences;

    }

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ROLE = "userRole";

    private static final String KEY_AIR_PRESSURE_ENABLED = "isAirPressureEnabled";
    private static final String KEY_HUMIDITY_ENABLED = "isHumidityEnabled";
    private static final String KEY_TEMPERATURE_ENABLED = "isTemperatureEnabled";

    public static void setLoginState(boolean isLoggedIn){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving login state
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn); // or false if not logged in
        editor.apply();
    }

    public static boolean isLoggedIn(){
        boolean isLoggedIn = getSharedPreference().getBoolean(KEY_IS_LOGGED_IN, false);
        return isLoggedIn;
    }

    public static void setAdmin(boolean isAdmin){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving login state
        editor.putBoolean(KEY_USER_ROLE, true); // or false if not logged in
        editor.apply();
    }



//    public static boolean isAirEnabled(){
//        boolean isAirEnabled = getSharedPreference().getBoolean(KEY_AIR_PRESSURE_ENABLED, false);
//        return isAirEnabled;
//    }
//
//    public static void setAirSensor(boolean isAirEnabled){
//        SharedPreferences.Editor editor = getSharedPreference().edit();
//
//        editor.putBoolean(KEY_AIR_PRESSURE_ENABLED, true); // or false if not enabled
//        editor.apply();
//    }
//
//    public static boolean isTempEnabled(){
//        boolean isTempEnabled = getSharedPreference().getBoolean(KEY_TEMPERATURE_ENABLED, false);
//        return isTempEnabled;
//    }
//
//    public static void setTempSensor(boolean isTempEnabled){
//        SharedPreferences.Editor editor = getSharedPreference().edit();
//
//        editor.putBoolean(KEY_TEMPERATURE_ENABLED, isTempEnabled); // or false if not enabled
//        editor.apply();
//    }
//
//    public static boolean isHumidityEnabled(){
//        boolean isHumidityEnabled = getSharedPreference().getBoolean(KEY_HUMIDITY_ENABLED, false);
//        return isHumidityEnabled;
//    }
//
//    public static void setHumiditySensor(boolean isHumidityEnabled){
//        SharedPreferences.Editor editor = getSharedPreference().edit();
//
//        editor.putBoolean(KEY_HUMIDITY_ENABLED, true); // or false if not enabled
//        editor.apply();
//    }

}


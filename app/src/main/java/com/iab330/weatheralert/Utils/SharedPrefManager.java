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

    // Whether or not the Sensors are activated on launch
    private static final String KEY_AIR_PRESSURE_ENABLED = "isAirPressureEnabled";
    private static final String KEY_HUMIDITY_ENABLED = "isHumidityEnabled";
    private static final String KEY_TEMPERATURE_ENABLED = "isTemperatureEnabled";

    // Whether or not the sensor data is displayed on the home screen
    private static final String KEY_AIR_PRESSURE_DISPLAYED = "isAirPressureDisplayed";
    private static final String KEY_HUMIDITY_DISPLAYED = "isHumidityDisplayed";
    private static final String KEY_TEMPERATURE_DISPLAYED = "isTemperatureDisplayed";

    // Supplementary preferences like Dark mode and fahrenheit
    private static final String KEY_DARKMODE_ENABLED = "isDarkModeEnabled";

    private static final String KEY_DYNAMIC_HOME_ENABLED = "isDynamicHomeEnabled";
    private static final String KEY_FAHRENHEIT_ENABLED = "isFahrenheitEnabled";

    private static final String KEY_AIR_PRESSURE_SENSITIVITY = "currentAirPressureSensitivity";

    public static void setLoginState(boolean isLoggedIn){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving login state
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn); // or false if not logged in
        editor.apply();
    }

    public static boolean isLoggedIn(){
        return getSharedPreference().getBoolean(KEY_IS_LOGGED_IN, false);
    }


    public static boolean isDarkModeEnabled(){
        return getSharedPreference().getBoolean(KEY_DARKMODE_ENABLED, false);
    }

    public static void setDarkMode(boolean isDarkModeEnabled){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving Dark mode
        editor.putBoolean(KEY_DARKMODE_ENABLED, isDarkModeEnabled);
        editor.apply();
    }

    public static boolean isDynamicHomeEnabled(){
        return getSharedPreference().getBoolean(KEY_DYNAMIC_HOME_ENABLED, false);
    }

    public static void setDynamicHome(boolean isDynamicHomeEnabled){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        // Saving Dark mode
        editor.putBoolean(KEY_DYNAMIC_HOME_ENABLED, isDynamicHomeEnabled);
        editor.apply();
    }

    public static boolean isAirEnabled(){
        return getSharedPreference().getBoolean(KEY_AIR_PRESSURE_ENABLED, false);
    }

    public static void setAirSensor(boolean isAirEnabled){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_AIR_PRESSURE_ENABLED, isAirEnabled); // or false if not enabled
        editor.apply();
    }

    public static boolean isTempEnabled(){
        return getSharedPreference().getBoolean(KEY_TEMPERATURE_ENABLED, false);
    }

    public static void setTempSensor(boolean isTempEnabled){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_TEMPERATURE_ENABLED, isTempEnabled); // or false if not enabled
        editor.apply();
    }

    public static boolean isHumidityEnabled(){
        return getSharedPreference().getBoolean(KEY_HUMIDITY_ENABLED, false);
    }

    public static void setHumiditySensor(boolean isHumidityEnabled){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_HUMIDITY_ENABLED, isHumidityEnabled); // or false if not enabled
        editor.apply();
    }

    public static boolean isAirDisplayed(){
        return getSharedPreference().getBoolean(KEY_AIR_PRESSURE_DISPLAYED, false);
    }

    public static void setAirDisplayed(boolean isAirDisplayed){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_AIR_PRESSURE_DISPLAYED, isAirDisplayed); // or false if not enabled
        editor.apply();
    }

    public static boolean isTempDisplayed(){
        return getSharedPreference().getBoolean(KEY_TEMPERATURE_DISPLAYED, false);
    }

    public static void setTempDisplayed(boolean isTempDisplayed){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_TEMPERATURE_DISPLAYED, isTempDisplayed); // or false if not enabled
        editor.apply();
    }

    public static boolean isHumidityDisplayed(){
        return getSharedPreference().getBoolean(KEY_HUMIDITY_DISPLAYED, false);
    }

    public static void setHumidityDisplayed(boolean isHumidityDisplayed){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_HUMIDITY_DISPLAYED, isHumidityDisplayed); // or false if not enabled
        editor.apply();
    }

    public static boolean isFahreheitEnabled(){
        return getSharedPreference().getBoolean(KEY_FAHRENHEIT_ENABLED, false);
    }
    public static void setFahrenheitEnabled(boolean isFahrenheitEnabled){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putBoolean(KEY_FAHRENHEIT_ENABLED, isFahrenheitEnabled);
        editor.apply();
    }

    public static float currentAirPressureSensitivity(){
        return getSharedPreference().getFloat(KEY_AIR_PRESSURE_SENSITIVITY, 1000);
    }

    public static void setAirPressureSensitivity(float currentAirPressureSensitivity){
        SharedPreferences.Editor editor = getSharedPreference().edit();

        editor.putFloat(KEY_AIR_PRESSURE_SENSITIVITY, currentAirPressureSensitivity);
        editor.apply();
    }

}


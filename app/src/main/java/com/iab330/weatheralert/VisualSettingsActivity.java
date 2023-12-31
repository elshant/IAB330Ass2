package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.iab330.weatheralert.Utils.SharedPrefManager;

public class VisualSettingsActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;

    private SwitchCompat darkmodeSwitch;
    private SwitchCompat tempDisplaySwitch;
    private SwitchCompat pressureDisplaySwitch;
    private SwitchCompat humidDisplaySwitch;

    private SwitchCompat dynamicHomeSwitch;
    private LinearLayout mainLayout;
    private FrameLayout footerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_settings);
        setViewIds();

        handleAlertClick();
        handleSettingClick();
        handleHomeClick();

        if (SharedPrefManager.isDarkModeEnabled()){
            mainLayout.setBackgroundColor(Color.DKGRAY);
            footerTab.setBackgroundColor(Color.GRAY);
        }
        else{
            mainLayout.setBackgroundColor(Color.WHITE);
            footerTab.setBackgroundColor(Color.DKGRAY);
        }

        handleDarkMode();
        handleDynamicHome();
        handleTempDisplay();
        handleAirDisplay();
        handleHumidDisplay();
    }

    private void handleDarkMode(){
        darkmodeSwitch.toggle();
        darkmodeSwitch.setOnClickListener(view -> {
            if (darkmodeSwitch.isChecked()){
                mainLayout.setBackgroundColor(Color.DKGRAY);
                footerTab.setBackgroundColor(Color.GRAY);
                SharedPrefManager.setDarkMode(true);
            }
            else{
                mainLayout.setBackgroundColor(Color.WHITE);
                footerTab.setBackgroundColor(Color.DKGRAY);
                SharedPrefManager.setDarkMode(false);
            }
        });
    }

    private void handleDynamicHome(){
        dynamicHomeSwitch.setOnClickListener(view -> {
            SharedPrefManager.setDynamicHome(dynamicHomeSwitch.isChecked());
        });
        if (SharedPrefManager.isDynamicHomeEnabled()){
            dynamicHomeSwitch.setChecked(true);
        }
        else{
            dynamicHomeSwitch.setChecked(false);
        }
    }

    private void handleTempDisplay(){
        tempDisplaySwitch.setOnClickListener(view -> {
            SharedPrefManager.setTempDisplayed(tempDisplaySwitch.isChecked());
        });

        if (SharedPrefManager.isTempDisplayed()){
            tempDisplaySwitch.setChecked(true);
        }
        else{
            tempDisplaySwitch.setChecked(false);
        }
    }
    private void handleAirDisplay(){
        pressureDisplaySwitch.setOnClickListener(view -> {
            SharedPrefManager.setAirDisplayed(pressureDisplaySwitch.isChecked());
        });
        if (SharedPrefManager.isAirDisplayed()){
            pressureDisplaySwitch.setChecked(true);
        }
        else{
            pressureDisplaySwitch.setChecked(false);
        }
    }
    private void handleHumidDisplay(){
        humidDisplaySwitch.setOnClickListener(view -> {
            SharedPrefManager.setHumidityDisplayed(humidDisplaySwitch.isChecked());
        });

        if (SharedPrefManager.isHumidityDisplayed()){
            humidDisplaySwitch.setChecked(true);
        }
        else{
            humidDisplaySwitch.setChecked(false);
        }
    }

    private void handleHomeClick(){
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
    private void handleAlertClick() {
        btnAlert.setOnClickListener(view -> {
            Intent intent = new Intent(this, AlertActivity.class);
            startActivity(intent);
        });
    }
    private void handleSettingClick(){
        btnSetting.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsMenuActivity.class);
            startActivity(intent);
        });
    }
    private void setViewIds(){
        btnHome = findViewById(R.id.homeBtn);
        btnSetting = findViewById(R.id.settingsBtn);
        btnAlert = findViewById(R.id.alertBtn);

        darkmodeSwitch = findViewById(R.id.darkModeEnabled);
        dynamicHomeSwitch = findViewById(R.id.dynamicWallpaperEnabled);
        tempDisplaySwitch = findViewById(R.id.temperatureDisplayEnabled);
        pressureDisplaySwitch = findViewById(R.id.airPressureDisplayEnabled);
        humidDisplaySwitch = findViewById(R.id.humidityDisplayEnabled);

        mainLayout = findViewById(R.id.visualSettings);
        footerTab = findViewById(R.id.footerTab);
    }
}
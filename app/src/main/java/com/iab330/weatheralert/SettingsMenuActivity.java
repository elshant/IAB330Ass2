package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.iab330.weatheralert.Utils.SharedPrefManager;

public class SettingsMenuActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;
    private LinearLayout mainLayout;
    private FrameLayout footerTab;

    Button visualsBtn;
    Button profileBtn;
    Button sensorBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        setViewIds();
        if (SharedPrefManager.isDarkModeEnabled()){
            mainLayout.setBackgroundColor(Color.DKGRAY);
            footerTab.setBackgroundColor(Color.GRAY);
        }
        else{
            mainLayout.setBackgroundColor(Color.WHITE);
            footerTab.setBackgroundColor(Color.DKGRAY);
        }
        handleAlertClick();
        handleSettingClick();
        handleHomeClick();
        handleVisualClick();
        handleProfileClick();
        handleSensorClick();
    }

    private void handleVisualClick(){
        visualsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, VisualSettingsActivity.class);
            startActivity(intent);
        });
    }
    private void handleProfileClick(){
        profileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            startActivity(intent);
        });
    }
    private void handleSensorClick(){
        sensorBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, SensorSettingsActivity.class);
            startActivity(intent);
        });
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
        visualsBtn = findViewById(R.id.visualBtn);
        profileBtn = findViewById(R.id.profileBtn);
        sensorBtn = findViewById(R.id.sensorBtn);

        mainLayout = findViewById(R.id.settingsMenu);
        footerTab = findViewById(R.id.footerTab);
    }
}
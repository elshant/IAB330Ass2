package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingsMenuActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;

    Button visualsBtn;
    Button profileBtn;
    Button sensorBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        setViewIds();
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
    }
}
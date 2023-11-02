package com.iab330.weatheralert;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.iab330.weatheralert.DB.AirPressureDao;
import com.iab330.weatheralert.DB.AirPressureData;
import com.iab330.weatheralert.DB.HumidityDao;
import com.iab330.weatheralert.DB.HumidityData;
import com.iab330.weatheralert.DB.TemperatureDao;
import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;
import com.iab330.weatheralert.Utils.MyApp;
import com.iab330.weatheralert.Utils.SharedPrefManager;



public class SensorSettingsActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;

    private SwitchCompat switchTemp;
    private SwitchCompat switchFahren;
    private SeekBar airPressureSensitivity;
    private SwitchCompat switchAir;
    private SwitchCompat switchHumid;

    private SensorManager sensorManager;
    private float prevAirPressure;
    private LinearLayout mainLayout;
    private FrameLayout footerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);
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

        handleTempSensorClick();
        handleFahrenheitSwitch();
        handleAirSensorClick();
        handleHumidSensorClick();

        if (SharedPrefManager.isTempEnabled()){
            switchTemp.setChecked(true);
        }
        else{
            switchTemp.setChecked(false);
        }

        if (SharedPrefManager.isAirEnabled()){
            switchAir.setChecked(true);
        }
        else{
            switchAir.setChecked(false);
        }

        if (SharedPrefManager.isHumidityEnabled()){
            switchHumid.setChecked(true);
        }
        else{
            switchHumid.setChecked(false);
        }
    }

    private void handleTempSensorClick() {
        switchTemp.toggle();
        switchTemp.setOnClickListener(view -> {
            if (switchTemp.isChecked()) {
                Log.d("Sensor data ", "Temperature Sensor Activated");
                SharedPrefManager.setTempSensor(true);
            } else if (!switchTemp.isChecked()) {
                Log.d("Sensor data ", "Temperature Sensor Deactivated");
                SharedPrefManager.setTempSensor(false);
            }
        });


    }

    private void handleAirSensorClick() {
        switchAir.toggle();
        switchAir.setOnClickListener(view -> {
            if (switchAir.isChecked()) {
                Log.d("Sensor data ", "Air pressure Sensor Activated");
                SharedPrefManager.setAirSensor(true);
            } else if (!switchAir.isChecked()) {
                Log.d("Sensor data ", "Air pressure Sensor Deactivated");
                SharedPrefManager.setAirSensor(false);
            }
        });
    }
    private void handleAirPressureSensitivity(){
        int sensitivity = airPressureSensitivity.getProgress();
        Log.d("Sensor Data", "Pressure sensitivity changed: " + sensitivity);
    }
    private void handleFahrenheitSwitch(){
        switchFahren.toggle();
        switchFahren.setOnClickListener(view -> SharedPrefManager.setFahrenheitEnabled(switchFahren.isChecked()));
    }

    private void handleHumidSensorClick() {
        switchHumid.toggle();
        switchHumid.setOnClickListener(view -> {
            if (switchHumid.isChecked()) {
                Log.d("Sensor data ", "Humidity Sensor Activated");
                SharedPrefManager.setHumiditySensor(true);
            } else if (!switchHumid.isChecked()) {
                Log.d("Sensor data ", "Humidity Sensor Deactivated");
                SharedPrefManager.setHumiditySensor(false);
            }
        });
    }

    private void handleHomeClick() {
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

    private void handleSettingClick() {
        btnSetting.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsMenuActivity.class);
            startActivity(intent);
        });
    }

    private void setViewIds() {
        btnHome = findViewById(R.id.homeBtn);
        btnSetting = findViewById(R.id.settingsBtn);
        btnAlert = findViewById(R.id.alertBtn);

        switchAir = findViewById(R.id.airPressureEnabled);
        switchTemp = findViewById(R.id.temperatureEnabled);
        switchHumid = findViewById(R.id.humidityEnabled);

        mainLayout = findViewById(R.id.sensorSettings);
        footerTab = findViewById(R.id.footerTab);
        airPressureSensitivity = findViewById(R.id.airPressureSensitivity);
        switchFahren = findViewById(R.id.tempFahrenheitEnabled);
    }

}
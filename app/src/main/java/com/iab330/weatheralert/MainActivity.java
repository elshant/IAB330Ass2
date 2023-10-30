package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageButton btnHome;
    ImageButton btnAlert;
    ImageButton btnSetting;
    TextView dataLink;
    Intent service;
    SensorDataReceiver dataReceiver;
    SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewIds();
        handleAlertClick();
        handleSettingClick();
        handleHomeClick();
        handleDataClick();
        manageSensorService();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class SensorDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA")){
                // Receive the sensor data

                TemperatureData temperatureData = (TemperatureData)
                        intent.getSerializableExtra("temperatureData");

            }
        }
    }
    private void manageSensorService(){
        service = new Intent(this, SensorService.class);
        startService(service);

        dataReceiver = new SensorDataReceiver();
        IntentFilter filter = new IntentFilter("WEATHER_SENSOR_DATA");
        registerReceiver(dataReceiver, filter);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (dataReceiver != null){
            unregisterReceiver(dataReceiver);
        }
    }



    private void handleDataClick(){
        dataLink.setOnClickListener(view -> {
            Intent intent = new Intent(this, DataActivity.class);
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
        dataLink = findViewById(R.id.detailsText);
    }
}
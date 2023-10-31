package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageButton btnHome;
    ImageButton btnAlert;
    ImageButton btnSetting;
    TextView dataLink;
    Intent service;
    SensorDataReceiver dataReceiver;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewIds();
        handleBackgroundChange();
        handleAlertClick();
        handleSettingClick();
        handleHomeClick();
        handleDataClick();
        manageSensorService();
    }

    private class SensorDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA_TEMP")){
                // Receive the sensor data
                String temperatureData = intent.getStringExtra("temperatureData");

                Log.d("Received Temperature", "Received" + temperatureData);

                TextView temperatureDisplay = findViewById(R.id.temperature);
                temperatureDisplay.setText(temperatureData + "°C");
            } else if(intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA_AIR_PRESSURE")){
                // Receive the sensor data
                String airPressureData = intent.getStringExtra("airPressureData");

                Log.d("Received Pressure", "Received" + airPressureData);

                TextView airPressureDisplay = findViewById(R.id.airpressure);
                airPressureDisplay.setText(airPressureData + "hPa");
            } else if(intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA_HUMIDITY")){
                // Receive the sensor data
                String humidityData = intent.getStringExtra("humidityData");

                Log.d("Received Humidity", "Received" + humidityData);

                TextView humidityDisplay = findViewById(R.id.humidity);
                humidityDisplay.setText(humidityData + "%");
            }
        }
    }
    private void manageSensorService(){
        service = new Intent(this, SensorService.class);
        startService(service);

        dataReceiver = new SensorDataReceiver();
        IntentFilter filterTemp = new IntentFilter("WEATHER_SENSOR_DATA_TEMP");
        registerReceiver(dataReceiver, filterTemp);
        IntentFilter filterPressure = new IntentFilter("WEATHER_SENSOR_DATA_AIR_PRESSURE");
        registerReceiver(dataReceiver, filterPressure);
        IntentFilter filterHumidity = new IntentFilter("WEATHER_SENSOR_DATA_HUMIDITY");
        registerReceiver(dataReceiver, filterHumidity);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (dataReceiver != null){
            unregisterReceiver(dataReceiver);
        }
    }

    private void handleHomeClick(){
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void handleDataClick(){
        dataLink.setOnClickListener(view -> {
            Intent intent = new Intent(this, DataActivity.class);
            startActivity(intent);
        });
    }

    private void handleBackgroundChange() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        Log.d("timeOfDay", String.valueOf(timeOfDay));

        TextView timeOfDay2 = findViewById(R.id.timeOfDay);

        if(timeOfDay >= 8 && timeOfDay < 16){
            mainLayout.setBackgroundResource(R.drawable.background_2);
            setColour("Midday");
            timeOfDay2.setText("Midday,");
        }else if(timeOfDay >= 16 && timeOfDay < 20){
            mainLayout.setBackgroundResource(R.drawable.background_3);
            setColour("Evening");
            timeOfDay2.setText("Evening,");
        }else if(timeOfDay >= 0 && timeOfDay < 4 || timeOfDay >= 20 && timeOfDay < 24){
            mainLayout.setBackgroundResource(R.drawable.background_4);
            setColour("Night");
            timeOfDay2.setText("Night,");
        }else if(timeOfDay >= 4 && timeOfDay < 8){
            mainLayout.setBackgroundResource(R.drawable.background_1);
            setColour("Dawn");
            timeOfDay2.setText("Dawn,");
        }
    }

    private void setColour(String timeframe) {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        LinearLayout conditionsDisplay = findViewById(R.id.conditionsDisplay);
        LinearLayout sensorDataDisplay = findViewById(R.id.sensorDataDisplay);
        LinearLayout sensorDataDisplayPressure = findViewById(R.id.sensorDataDisplayPressure);
        String currentColor;

        if(Objects.equals(timeframe, "Midday") || Objects.equals(timeframe, "Evening")) {
            currentColor = "#373D3F";
        } else if(Objects.equals(timeframe, "Night")) {
            currentColor = "#D9D9D9";
        } else {
            currentColor = "F2F3F4";
        }

        for (int i = 0; i < mainLayout.getChildCount() && i < 4; i++) {
            View v = mainLayout.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(Color.parseColor(currentColor));
            }
        }

        for (int i = 0; i < conditionsDisplay.getChildCount(); i++) {
            View v = conditionsDisplay.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(Color.parseColor(currentColor));
            }
        }

        for (int i = 0; i < sensorDataDisplay.getChildCount(); i++) {
            View v = sensorDataDisplay.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(Color.parseColor(currentColor));
            }
        }

        for (int i = 0; i < sensorDataDisplayPressure.getChildCount(); i++) {
            View v = sensorDataDisplayPressure.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(Color.parseColor(currentColor));
            }
        }
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
        mainLayout = findViewById(R.id.mainLayout);
    }
}
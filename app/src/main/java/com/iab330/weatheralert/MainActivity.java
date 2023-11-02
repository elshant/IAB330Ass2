package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iab330.weatheralert.DB.AirPressureDao;
import com.iab330.weatheralert.DB.AirPressureData;
import com.iab330.weatheralert.DB.HumidityDao;
import com.iab330.weatheralert.DB.HumidityData;
import com.iab330.weatheralert.DB.TemperatureDao;
import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;
import com.iab330.weatheralert.Utils.MyApp;
import com.iab330.weatheralert.Utils.SharedPrefManager;
import com.iab330.weatheralert.SensorSettingsActivity;

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
    TextView temperature;
    TextView humidity;
    TextView airPressure;
    ImageView tempImg;
    ImageView humidImg;
    ImageView airImg;
    private float prevAirPressure = 0;

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
        handleSensorDisplay();
        handleSensorEnabled();

        //getLatestTemperatureData();

    }

    private class SensorDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA_TEMP")){
                // Receive the sensor data
                String temperatureData = intent.getStringExtra("temperatureData");

                Log.d("Received Temperature", "Received" + temperatureData);

                if (SharedPrefManager.isTempDisplayed()){
                    if (SharedPrefManager.isFahreheitEnabled()){
                        temperature.setText(temperatureData + "°F");
                    }
                    else{
                        temperature.setText(temperatureData + "°C");
                    }
                }
                else{
                    tempImg.setVisibility(ImageView.GONE);
                    temperature.setVisibility(TextView.GONE);
                }

            } else if(intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA_AIR_PRESSURE")){
                // Receive the sensor data
                String airPressureData = intent.getStringExtra("airPressureData");

                Log.d("Received Pressure", "Received" + airPressureData);

                if (SharedPrefManager.isAirDisplayed()){
                    airPressure.setText(airPressureData + "hPa");
                }
                else{
                    airImg.setVisibility(ImageView.GONE);
                    airPressure.setVisibility(TextView.GONE);
                }

            } else if(intent.getAction() != null && intent.getAction().equals("WEATHER_SENSOR_DATA_HUMIDITY")){
                // Receive the sensor data
                String humidityData = intent.getStringExtra("humidityData");

                Log.d("Received Humidity", "Received" + humidityData);

                if (SharedPrefManager.isHumidityDisplayed()){
                    humidity.setText(humidityData + "%");
                }
                else{
                    humidImg.setVisibility(ImageView.GONE);
                    humidity.setVisibility(TextView.GONE);
                }
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

    private void handleSensorDisplay() {
        if (SharedPrefManager.isTempDisplayed()){
            tempImg.setVisibility(ImageView.VISIBLE);
            temperature.setVisibility(TextView.VISIBLE);
        }
        else{
            tempImg.setVisibility(ImageView.GONE);
            temperature.setVisibility(TextView.GONE);
        }

        if (SharedPrefManager.isHumidityDisplayed()){
            humidImg.setVisibility(ImageView.VISIBLE);
            humidity.setVisibility(TextView.VISIBLE);
        }
        else{
            humidImg.setVisibility(ImageView.GONE);
            humidity.setVisibility(TextView.GONE);
        }

        if (SharedPrefManager.isAirDisplayed()){
            airImg.setVisibility(ImageView.VISIBLE);
            airPressure.setVisibility(TextView.VISIBLE);
        }
        else{
            airImg.setVisibility(ImageView.GONE);
            airPressure.setVisibility(TextView.GONE);
        }
    }
    private void handleSensorEnabled() {
        if (SharedPrefManager.isTempEnabled()){
            senseOnce("temp");
        }
        else{
            temperature.setText("N/A");
        }

        if (SharedPrefManager.isAirEnabled()){
            senseOnce("air");
        }
        else{
            airPressure.setText("N/A");
        }

        if (SharedPrefManager.isHumidityEnabled()){
            senseOnce("humidity");
        }
        else{
            humidity.setText("N/A");
        }
    }

    private void senseOnce(String sensorType) {

    }

    private void setViewIds(){
        btnHome = findViewById(R.id.homeBtn);
        btnSetting = findViewById(R.id.settingsBtn);
        btnAlert = findViewById(R.id.alertBtn);
        dataLink = findViewById(R.id.detailsText);
        mainLayout = findViewById(R.id.mainLayout);

        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        airPressure = findViewById(R.id.airpressure);

        tempImg = findViewById(R.id.tempImg);
        humidImg = findViewById(R.id.humidImg);
        airImg = findViewById(R.id.airImg);
    }

}
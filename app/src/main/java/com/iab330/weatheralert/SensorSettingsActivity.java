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



public class SensorSettingsActivity extends AppCompatActivity implements SensorEventListener {

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

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        handleTempSensorClick();
        handleFahrenheitSwitch();
        handleAirSensorClick();
        handleHumidSensorClick();

        if (SharedPrefManager.isTempEnabled()){
            switchTemp.setChecked(true);
            sensorManager.registerListener(this, SensorService.tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            switchTemp.setChecked(false);
        }

        if (SharedPrefManager.isAirEnabled()){
            switchAir.setChecked(true);
            sensorManager.registerListener(this, SensorService.airSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            switchAir.setChecked(false);
        }

        if (SharedPrefManager.isHumidityEnabled()){
            switchHumid.setChecked(true);
            sensorManager.registerListener(this, SensorService.humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{
            switchHumid.setChecked(false);
        }
    }

    private void handleTempSensorClick() {
        switchTemp.toggle();
        switchTemp.setOnClickListener(view -> {
            if (switchTemp.isChecked()) {
                sensorManager.registerListener(this, SensorService.tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("Sensor data ", "Temperature Sensor Activated");
                SharedPrefManager.setTempSensor(true);
            } else if (!switchTemp.isChecked()) {
                sensorManager.unregisterListener(this, SensorService.tempSensor);
                Log.d("Sensor data ", "Temperature Sensor Deactivated");
                SharedPrefManager.setTempSensor(false);
            }
        });


    }

    private void handleAirSensorClick() {
        switchAir.toggle();
        switchAir.setOnClickListener(view -> {
            if (switchAir.isChecked()) {
                sensorManager.registerListener(this, SensorService.airSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("Sensor data ", "Air pressure Sensor Activated");
                SharedPrefManager.setAirSensor(true);
            } else if (!switchAir.isChecked()) {
                sensorManager.unregisterListener(this, SensorService.airSensor);
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
                sensorManager.registerListener(this, SensorService.humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("Sensor data ", "Humidity Sensor Activated");
                SharedPrefManager.setHumiditySensor(true);
            } else if (!switchHumid.isChecked()) {
                sensorManager.unregisterListener(this, SensorService.humiditySensor);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temp = sensorEvent.values[0];

            // If fahrenheit is enabled
            if (SharedPrefManager.isFahreheitEnabled() || switchFahren.isChecked()){
                temp = (temp * 9/5) + 32;
            }

            TemperatureData temperatureData = new TemperatureData(temp, sensorEvent.timestamp);
            Intent broadcastIntent = new Intent("WEATHER_SENSOR_DATA_TEMP");
            broadcastIntent.putExtra("temperatureData", String.valueOf(temp));
            sendBroadcast(broadcastIntent);


            TemperatureDao temperatureDao = MyApp.getAppDatabase().temperatureDao();
            AsyncTask.execute(() -> temperatureDao.insertTemperature(temperatureData));
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float airPressure = sensorEvent.values[0];
            if (airPressure != prevAirPressure) {
                AirPressureData airPressureData = new AirPressureData(airPressure, sensorEvent.timestamp);
                Log.d("Sensed - pressure ", "true");
                Intent broadcastIntent = new Intent("WEATHER_SENSOR_DATA_AIR_PRESSURE");
                broadcastIntent.putExtra("airPressureData", String.valueOf(airPressure));
                sendBroadcast(broadcastIntent);

                AirPressureDao airPressureDao = MyApp.getAppDatabase().airPressureDao();
                AsyncTask.execute(() -> airPressureDao.insertAirPressure(airPressureData));
                prevAirPressure = airPressure;
            }
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float humid = sensorEvent.values[0];
            HumidityData humidityData = new HumidityData(humid, sensorEvent.timestamp);
            Intent broadcastIntent = new Intent("WEATHER_SENSOR_DATA_HUMIDITY");
            broadcastIntent.putExtra("humidityData", String.valueOf(humid));
            sendBroadcast(broadcastIntent);

            HumidityDao humidityDao = MyApp.getAppDatabase().humidityDao();
            AsyncTask.execute(() -> humidityDao.insertHumidity(humidityData));
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
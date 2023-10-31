package com.iab330.weatheralert;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageButton;
import android.widget.Switch;

import com.iab330.weatheralert.DB.AirPressureDao;
import com.iab330.weatheralert.DB.AirPressureData;
import com.iab330.weatheralert.DB.HumidityDao;
import com.iab330.weatheralert.DB.HumidityData;
import com.iab330.weatheralert.DB.TemperatureDao;
import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;
import com.iab330.weatheralert.Utils.MyApp;

import java.util.concurrent.ExecutorService;


public class SensorSettingsActivity extends AppCompatActivity implements SensorEventListener {

    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;

    private Switch switchTemp;
    private Switch switchAir;
    private Switch switchHumid;

    private SensorManager sensorManager;
    private float prevAirPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);
        setViewIds();


        handleAlertClick();
        handleSettingClick();
        handleHomeClick();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        handleTempSensorClick();
        handleAirSensorClick();
        handleHumidSensorClick();

        switchAir.setChecked(SensorService.airSensor != null);

        switchTemp.setChecked(SensorService.tempSensor != null);

        switchHumid.setChecked(SensorService.humiditySensor != null);
    }

    private void handleTempSensorClick() {
        switchTemp.toggle();
        switchTemp.setOnClickListener(view -> {
            if (switchTemp.isChecked()) {
                sensorManager.registerListener(this, SensorService.tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("Sensor data ", "Temperature Sensor Activated");
            } else if (!switchTemp.isChecked()) {
                sensorManager.unregisterListener(this, SensorService.tempSensor);
                Log.d("Sensor data ", "Temperature Sensor Deactivated");
            }
        });


    }

    private void handleAirSensorClick() {
        switchAir.toggle();
        switchAir.setOnClickListener(view -> {
            if (switchAir.isChecked()) {
                sensorManager.registerListener(this, SensorService.airSensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("Sensor data ", "Air pressure Sensor Activated");
            } else if (!switchAir.isChecked()) {
                sensorManager.unregisterListener(this, SensorService.airSensor);
                Log.d("Sensor data ", "Air pressure Sensor Deactivated");
            }
        });

    }

    private void handleHumidSensorClick() {
        switchHumid.toggle();
        switchHumid.setOnClickListener(view -> {
            if (switchHumid.isChecked()) {
                sensorManager.registerListener(this, SensorService.humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
                Log.d("Sensor data ", "Humidity Sensor Activated");
            } else if (!switchHumid.isChecked()) {
                sensorManager.unregisterListener(this, SensorService.humiditySensor);
                Log.d("Sensor data ", "Humidity Sensor Deactivated");
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
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temp = sensorEvent.values[0];
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
package com.iab330.weatheralert.SensorUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.iab330.weatheralert.DB.AirPressureDao;
import com.iab330.weatheralert.DB.AirPressureData;
import com.iab330.weatheralert.DB.HumidityDao;
import com.iab330.weatheralert.DB.HumidityData;
import com.iab330.weatheralert.DB.TemperatureDao;
import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.MainActivity;
import com.iab330.weatheralert.Utils.MyApp;
import com.iab330.weatheralert.Utils.SharedPrefManager;

import java.util.Arrays;

public class SensorService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    public static Sensor tempSensor = null;
    public static Sensor humiditySensor = null;
    public static Sensor airSensor = null;

    private float prevAirPressure;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            Toast.makeText(this, "Device does not have Temperature Sensor", Toast.LENGTH_SHORT).show();
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        } else {
            Toast.makeText(this, "Device does not have Humidity Sensor", Toast.LENGTH_SHORT).show();
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            airSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        } else {
            Toast.makeText(this, "Device does not have Air pressure Sensor", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (tempSensor != null && SharedPrefManager.isTempEnabled()){
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (humiditySensor != null && SharedPrefManager.isHumidityEnabled()){
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (airSensor != null && SharedPrefManager.isAirEnabled()) {
            sensorManager.registerListener(this, airSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager!=null){
            sensorManager.unregisterListener(this);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temp = sensorEvent.values[0];

            // If fahrenheit is enabled
            if (SharedPrefManager.isFahreheitEnabled()){
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

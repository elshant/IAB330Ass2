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

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){

            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        }else {
            Toast.makeText(this, "Device does not have Temperature Sensor", Toast.LENGTH_SHORT).show();
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){

            humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        }else {
            Toast.makeText(this, "Device does not have Humidity Sensor", Toast.LENGTH_SHORT).show();
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){
            airSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }
        else {
            Toast.makeText(this, "Device does not have Air pressure Sensor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        if (tempSensor != null){
//            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            SharedPrefManager.setTempSensor(true);
//        }
//
//        if (humiditySensor != null){
//            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
//            SharedPrefManager.setHumiditySensor(true);
//        }
//
//        if (airSensor != null) {
//            sensorManager.registerListener(this, airSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            SharedPrefManager.setAirSensor(true);
//        }
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
        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float temp = sensorEvent.values[0];
            TemperatureData temperatureData = new TemperatureData(temp, sensorEvent.timestamp);
            TemperatureDao temperatureDao = MyApp.getAppDatabase().temperatureDao();
//            AsyncTask.execute(()->{
//                temperatureDao.insertTemperature(temperatureData);
//            });
            Intent broadcastIntent = new Intent("WEATHER_SENSOR_DATA");
            broadcastIntent.putExtra("temperatureData", temp);
            sendBroadcast(broadcastIntent);
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
            float pressure = sensorEvent.values[0];
            Log.d("Sensor data ", "Ambient pressure is " + pressure);
            // Send data to the activity

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}

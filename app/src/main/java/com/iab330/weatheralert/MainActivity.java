package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;

public class MainActivity extends AppCompatActivity {

    ImageButton btnHome;
    ImageButton btnAlert;
    ImageButton btnSetting;
    TextView dataLink;
    Intent serviceIntent;
    SensorDatReceiver dataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewIds();
        handleAlertClick();
        handleSettingClick();
        handleHomeClick();
        handleDataClick();
        manageSensorData();
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

    private void manageSensorData() {

        Log.d("OnCreateTesting", "Manage Sensor Data Running ----------------");

        serviceIntent = new Intent(this, SensorService.class);
        startService(serviceIntent);

//        dataReceiver = new SensorDatReceiver();
//        IntentFilter filter = new IntentFilter("VEHICLE_SENSOR_DATA");
//        registerReceiver(dataReceiver, filter);

    }

    private class SensorDatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("VEHICLE_SENSOR_DATA")) {
                // Receive the sensor data and update the UI if required.
                Log.d("OnCreateTesting", "Manage Sensor Data Running - data received and not null ----------------");
                TemperatureData tempData = (TemperatureData)
                        intent.getSerializableExtra("tempData");
                // Making changes in the UI based on sensor data
                if (tempData != null && tempData.getTemp()
                        < 50){

                    getWindow().getDecorView().setBackgroundColor(getResources().getColor
                            (R.color.white, null));
                }else {
//                    Toast.makeText(VehicleListActivity.this, "Danger!!!",
//                            Toast.LENGTH_SHORT).show();

                    getWindow().getDecorView().setBackgroundColor(getResources().getColor
                            (R.color.red, null));
                }

            } else {
                Log.d("FAIL", "No lol ----------------");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dataReceiver);
    }
}
package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.iab330.weatheralert.DB.AirPressureData;
import com.iab330.weatheralert.DB.HumidityData;
import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.Utils.MyApp;
import com.iab330.weatheralert.Utils.SharedPrefManager;

import java.util.List;


public class AlertActivity extends AppCompatActivity{
    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;
    private final float TEMPERATURE_THRESHOLD = 30.0f;
    private final float HUMIDITY_THRESHOLD = 80.0f;
    private final float AIR_PRESSURE_THRESHOLD = SharedPrefManager.currentAirPressureSensitivity();
    private TextView alertTitleText, alertDescText, alertTitleTime, alertActionText;

    private ImageView noDataIcon1, noDataIcon2;
    private TextView noDataText1, noDataText2;

    private boolean hasAnyAlert = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        setViewIds();
        handleAlertClick();
        handleSettingClick();
        handleHomeClick();
        displayNoDataForEmergencyAlerts(!hasAnyAlert); // To show no data when there is no data passed
//Will fetch data from database
        checkTemperatureAlert();
        checkHumidityAlert();
        checkAirPressureAlert();
        noDataIcon1 = findViewById(R.id.noDataIcon1);
        noDataIcon2 = findViewById(R.id.noDataIcon2);
        noDataText1 = findViewById(R.id.noDataText1);
        noDataText2 = findViewById(R.id.noDataText2);


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
        alertTitleText = findViewById(R.id.alertTitleText);
        alertDescText = findViewById(R.id.alertDescText);
        alertTitleTime = findViewById(R.id.alertTitleTime);
        alertActionText = findViewById(R.id.alertActionText);

        noDataIcon1 = findViewById(R.id.noDataIcon1);
        noDataText1 = findViewById(R.id.noDataText1);

    }
    private void displayNoDataForEmergencyAlerts(boolean noData) { // No data
        if (noData) {
            noDataIcon1.setVisibility(View.VISIBLE);
            noDataText1.setVisibility(View.VISIBLE);
        } else {
            noDataIcon1.setVisibility(View.GONE);
            noDataText1.setVisibility(View.GONE);
        }
    }

    private void checkTemperatureAlert() { // fetches data from database and based on threshold defined above gives alerts
        LiveData<List<TemperatureData>> temperatureLiveData = MyApp.getAppDatabase().temperatureDao().getAllTemperatureData();
        temperatureLiveData.observe(this, temperatureDataList -> {
//            boolean hasData = false;
            for (TemperatureData temperatureData : temperatureDataList) {
                if (temperatureData.getTemp() > TEMPERATURE_THRESHOLD) {
                    hasAnyAlert = true;
                    // Update the UI components with alert details
                    alertTitleText.setText("High Temperature Alert");
                    alertDescText.setText("Temperature has reached: " + temperatureData.getTemp() + "°C");
                    alertActionText.setText("Consider staying indoors.");
                    // Assuming you have a method to get the time
                    alertTitleTime.setText(getCurrentFormattedTime());
                }
            }
            refreshNoDataUI();

        });

    }
    private void checkHumidityAlert() {
        LiveData<List<HumidityData>> humidityLiveData = MyApp.getAppDatabase().humidityDao().getAllHumidityData();
        humidityLiveData.observe(this, humidityDataList -> {
//            boolean hasData = false;
            for (HumidityData humidityData : humidityDataList) {
                if (humidityData.getHumid() > HUMIDITY_THRESHOLD) {
                    hasAnyAlert = true;
                    alertTitleText.setText("High Humidity Alert");
                    alertDescText.setText("Humidity level has reached: " + humidityData.getHumid() + "%");
                    alertActionText.setText("Consider using a dehumidifier.");
                    alertTitleTime.setText(getCurrentFormattedTime());
                }
            }
            refreshNoDataUI();

        });
    }

    private void checkAirPressureAlert() {
        LiveData<List<AirPressureData>> airPressureLiveData = MyApp.getAppDatabase().airPressureDao().getAllAirPressureData();
        airPressureLiveData.observe(this, airPressureDataList -> {
//            boolean hasData = false;
            for (AirPressureData airPressureData : airPressureDataList) {
                if (airPressureData.getAirPressure() < AIR_PRESSURE_THRESHOLD) {
                    hasAnyAlert = true;
                    alertTitleText.setText("Low Air Pressure Alert");
                    alertDescText.setText("Air pressure has reached: " + airPressureData.getAirPressure() + " hPa");
                    alertActionText.setText("Stay informed and watch for other weather conditions.");
                    alertTitleTime.setText(getCurrentFormattedTime());
                }
            }
            refreshNoDataUI();

        });
    }
    private void refreshNoDataUI() {
        displayNoDataForEmergencyAlerts(!hasAnyAlert);
    }

    private String getCurrentFormattedTime() { // method for time (returns current time)
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(now);
    }

}

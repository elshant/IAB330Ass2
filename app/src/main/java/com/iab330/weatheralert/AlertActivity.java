package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        FrameLayout footerTab = findViewById(R.id.footerTab);

        if (SharedPrefManager.isDarkModeEnabled()) {
            mainLayout.setBackgroundColor(Color.DKGRAY);
            footerTab.setBackgroundColor(Color.GRAY);
        }
        else {
            mainLayout.setBackgroundColor(Color.WHITE);
            footerTab.setBackgroundColor(Color.DKGRAY);
        }

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

    private void checkTemperatureAlert() {
        LiveData<TemperatureData> temperatureLiveData = MyApp.getAppDatabase().temperatureDao().getLatestTemperatureData();

        temperatureLiveData.observe(this, temperatureData -> {
            if (temperatureData != null && temperatureData.getTemp() > TEMPERATURE_THRESHOLD) {
                hasAnyAlert = true;
                alertTitleText.setText("High Temperature Alert");
                alertDescText.setText("Temperature has reached: " + temperatureData.getTemp() + "°C");
                alertActionText.setText("Consider staying indoors.");
                alertTitleTime.setText(getFormattedTime(temperatureData.getTimeStamp()));
            }
            refreshNoDataUI();
        });
    }

    private void checkHumidityAlert() {
        LiveData<HumidityData> humidityLiveData = MyApp.getAppDatabase().humidityDao().getLatestHumidityData();

        humidityLiveData.observe(this, humidityData -> {
            if (humidityData != null && humidityData.getHumid() > HUMIDITY_THRESHOLD) {
                hasAnyAlert = true;
                alertTitleText.setText("High Humidity Alert");
                alertDescText.setText("Humidity level has reached: " + humidityData.getHumid() + "%");
                alertActionText.setText("Consider using a dehumidifier.");
                alertTitleTime.setText(getFormattedTime(humidityData.getTimeStamp()));
            }
            refreshNoDataUI();
        });
    }

    private void checkAirPressureAlert() {
        LiveData<AirPressureData> airPressureLiveData = MyApp.getAppDatabase().airPressureDao().getLatestAirPressureData();

        airPressureLiveData.observe(this, airPressureData -> {
            if (airPressureData != null && airPressureData.getAirPressure() < AIR_PRESSURE_THRESHOLD) {
                hasAnyAlert = true;
                alertTitleText.setText("Low Air Pressure Alert");
                alertDescText.setText("Air pressure has reached: " + airPressureData.getAirPressure() + " hPa");
                alertActionText.setText("Stay informed and watch for other weather conditions.");
                alertTitleTime.setText(getFormattedTime(airPressureData.getTimeStamp()));
            }
            refreshNoDataUI();
        });
    }

    private void refreshNoDataUI() {

        displayNoDataForEmergencyAlerts(!hasAnyAlert);
    }

    private String getFormattedTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(date);
    }


}

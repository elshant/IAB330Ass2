package com.iab330.weatheralert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ImageButton;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.iab330.weatheralert.DB.TemperatureDao;
import com.iab330.weatheralert.DB.TemperatureData;
import com.iab330.weatheralert.SensorUtil.SensorService;
import com.iab330.weatheralert.Utils.MyApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataActivity extends AppCompatActivity {

    private ImageButton btnHome;
    private ImageButton btnAlert;
    private ImageButton btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        setViewIds();
        handleAlertClick();
        handleSettingClick();
        handleHomeClick();
        analyseTemperatureData();
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
    }

    private void analyseTemperatureData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        AnyChartView tempChartView = findViewById(R.id.tempChart);

        Cartesian tempLineChart = AnyChart.line();
        tempLineChart.animation(true); // Setting Animation for the chart
        tempLineChart.padding(10d, 20d, 5d, 20d);

        tempLineChart.crosshair().enabled(true);
        tempLineChart.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        tempLineChart.tooltip().positionMode(TooltipPositionMode.POINT);

        tempLineChart.title("Temperature analysis over the time.");
        tempLineChart.yAxis(0).title("Temperature (Degree celcius)");
        tempLineChart.xAxis(0).title("Timestamp");
        tempLineChart.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        tempLineChart.legend().enabled(true);
        tempLineChart.legend().fontSize(13d);
        tempLineChart.legend().padding(0d, 0d, 10d, 0d);

        List<DataEntry> seriesData = new ArrayList<>();
        Set set = Set.instantiate();
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = tempLineChart.line(series1Mapping);
        series1.name("Temperature Change");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        tempChartView.setChart(tempLineChart); // adding the chart to the chart view

        TemperatureDao temperatureDao = MyApp.getAppDatabase().temperatureDao();
        LiveData<List<TemperatureData>> temperatureLiveData = temperatureDao.getAllTemperatureData();
        temperatureLiveData.observe(this, temperatureDataList -> {
            for(TemperatureData temperatureData: temperatureDataList){
                Date date = new Date(temperatureData.getTimeStamp());
                seriesData.add(new CustomDataEntry(dateFormat.format(date), temperatureData.getTemp()));
            }
            set.data(seriesData);

        });
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }
}
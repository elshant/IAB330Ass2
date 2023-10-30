package com.iab330.weatheralert.DB;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "airPressureData")
public class AirPressureData {

    @PrimaryKey
    long timeStamp;
    float airPressure;

    public AirPressureData(float airPressure, long timeStamp) {
        this.airPressure = airPressure;
        this.timeStamp = timeStamp;
    }

    public float getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(float temp) {
        this.airPressure = temp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

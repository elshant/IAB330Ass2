package com.iab330.weatheralert.DB;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "humidityData")
public class HumidityData {

    @PrimaryKey
    long timeStamp;
    float humid;

    public HumidityData(float humid, long timeStamp) {
        this.humid = humid;
        this.timeStamp = timeStamp;
    }

    public float getHumid() {
        return humid;
    }

    public void setHumid(float humid) {
        this.humid = humid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

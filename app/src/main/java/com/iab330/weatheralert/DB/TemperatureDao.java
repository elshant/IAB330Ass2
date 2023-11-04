package com.iab330.weatheralert.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TemperatureDao {

    @Insert
    void insertTemperature(TemperatureData temperatureData);

    @Query("SELECT * FROM temperatureData")
    LiveData<List<TemperatureData>> getAllTemperatureData();

    @Query("DELETE FROM temperatureData")
    void deleteAllTemperatureData();

    // this is for alerts section to only get the latest data
    @Query("SELECT * FROM temperatureData ORDER BY timeStamp DESC LIMIT 1")
    LiveData<TemperatureData> getLatestTemperatureData();

}

package com.iab330.weatheralert.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HumidityDao {

    @Insert
    void insertHumidity(HumidityData humidityData);

    @Query("SELECT * FROM humidityData")
    LiveData<List<HumidityData>> getAllHumidityData();

    @Query("DELETE FROM humidityData")
    void deleteAllHumidityData();
    @Query("SELECT * FROM humidityData ORDER BY timeStamp DESC LIMIT 1")
    LiveData<HumidityData> getLatestHumidityData();

}

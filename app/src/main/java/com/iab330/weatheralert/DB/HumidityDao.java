package com.iab330.weatheralert.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HumidityDao {

    @Insert
    void insertTemperature(HumidityData humidityData);

    @Query("SELECT * FROM humidityData")
    LiveData<List<HumidityData>> getAllHumidityData();

    @Query("DELETE FROM humidityData")
    void deleteAllHumidityData();

}

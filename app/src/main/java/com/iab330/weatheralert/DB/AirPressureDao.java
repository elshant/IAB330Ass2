package com.iab330.weatheralert.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AirPressureDao {

    @Insert
    void insertAirPressure(AirPressureData airPressureData);

    @Query("SELECT * FROM airPressureData")
    LiveData<List<AirPressureData>> getAllAirPressureData();

    @Query("DELETE FROM airPressureData")
    void deleteAllAirPressureData();

}

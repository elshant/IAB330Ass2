package com.iab330.weatheralert.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.iab330.weatheralert.DB.TemperatureDao;
import com.iab330.weatheralert.DB.TemperatureData;

@Database(entities = {TemperatureData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    //public abstract VehicleDao vehicleDao();
    //public abstract AccelerometerDao accelerometerDao();
    public abstract TemperatureDao temperatureDao();
}


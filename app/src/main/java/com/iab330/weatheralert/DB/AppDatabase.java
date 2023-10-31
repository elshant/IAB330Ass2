package com.iab330.weatheralert.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {AirPressureData.class, HumidityData.class, TemperatureData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TemperatureDao temperatureDao();
    public abstract HumidityDao humidityDao();
    public abstract AirPressureDao airPressureDao();
}


package com.iab330.weatheralert.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {TemperatureData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TemperatureDao temperatureDao();
}


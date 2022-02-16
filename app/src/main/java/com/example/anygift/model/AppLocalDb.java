package com.example.anygift.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.anygift.MyApplication;

@Database(entities = {GiftCard.class}, version = 6)
    abstract class AppLocalDbRepository extends RoomDatabase {
        //public abstract UserDao userDao();
        public abstract GiftCardDao giftCardDao();

    }
    public class AppLocalDb{
        static public AppLocalDbRepository db =
                Room.databaseBuilder(MyApplication.getContext(),
                        AppLocalDbRepository.class,
                        "dbFileName.db")
                        .fallbackToDestructiveMigration()
                        .build();
    }

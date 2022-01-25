package com.example.anygift.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GiftCardDao {
    @Query("select * from GiftCard")
    List<GiftCard> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(GiftCard... giftCards);

    @Delete
    void delete(GiftCard giftCard);
}

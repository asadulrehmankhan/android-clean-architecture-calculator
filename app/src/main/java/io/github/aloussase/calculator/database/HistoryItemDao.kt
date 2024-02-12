package io.github.aloussase.calculator.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.aloussase.calculator.data.HistoryItem

@Dao
interface HistoryItemDao {
    @Query("SELECT * from history_items")
    suspend fun findAll(): List<HistoryItem>

    @Query("DELETE from history_items")
    suspend fun clear()

    @Insert
    suspend fun insertOne(item: HistoryItem)
}
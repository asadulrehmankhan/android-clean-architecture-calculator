package io.github.aloussase.calculator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.aloussase.calculator.data.HistoryItem

@Database(
    entities = [HistoryItem::class],
    version = 1
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyItemDao(): HistoryItemDao
}
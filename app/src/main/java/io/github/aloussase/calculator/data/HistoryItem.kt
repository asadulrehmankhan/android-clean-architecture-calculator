package io.github.aloussase.calculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "history_items"
)
data class HistoryItem(
    val content: String,
    @PrimaryKey val id: Int? = null,
)

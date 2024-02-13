package io.github.aloussase.calculator.repository

import io.github.aloussase.calculator.data.HistoryItem

interface ICalculatorRepository {
    suspend fun getAllHistoryItems(): List<HistoryItem>

    suspend fun createHistoryItem(item: HistoryItem): Long

    suspend fun deleteAllHistoryItems()

    suspend fun deleteOneItem(itemId: Long)

    suspend fun calculate(expr: String): Float?

}
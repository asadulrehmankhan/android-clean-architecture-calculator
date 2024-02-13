package io.github.aloussase.calculator.repository

import io.github.aloussase.calculator.data.HistoryItem

final class NullCalculatorRepository : ICalculatorRepository {
    override suspend fun getAllHistoryItems(): List<HistoryItem> {
        return listOf(
            HistoryItem("1+2", 1),
            HistoryItem("3+3", 2),
            HistoryItem("4+3", 3)
        )
    }

    override suspend fun createHistoryItem(item: HistoryItem): Long {
        return 0
    }

    override suspend fun deleteAllHistoryItems() {
    }

    override suspend fun deleteOneItem(itemId: Long) {
    }

    override suspend fun calculate(expr: String): Float {
        return 0f
    }
}
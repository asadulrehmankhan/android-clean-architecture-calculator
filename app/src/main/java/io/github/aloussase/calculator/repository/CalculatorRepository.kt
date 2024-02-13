package io.github.aloussase.calculator.repository

import android.util.Log
import io.github.aloussase.calculator.api.MathApi
import io.github.aloussase.calculator.api.MathApiRequestBody
import io.github.aloussase.calculator.data.HistoryItem
import io.github.aloussase.calculator.database.HistoryDatabase

class CalculatorRepository(
    val mathApi: MathApi,
    val historyDatabase: HistoryDatabase
) : ICalculatorRepository {
    override suspend fun getAllHistoryItems(): List<HistoryItem> {
        return historyDatabase.historyItemDao().findAll()
    }

    override suspend fun createHistoryItem(item: HistoryItem): Long {
        return historyDatabase.historyItemDao().insertOne(item)
    }

    override suspend fun deleteAllHistoryItems() {
        historyDatabase.historyItemDao().clear()
    }

    override suspend fun deleteOneItem(itemId: Long) {
        historyDatabase.historyItemDao().delete(itemId)
    }

    override suspend fun calculate(expr: String): Float? {
        Log.d("CalculatorRepository", "Calculating expression: $expr")

        val result = mathApi.calculateExpr(
            MathApiRequestBody(
                expr = expr
            )
        )

        if (result.error != null) {
            return null
        }

        return result.result?.toFloatOrNull()
    }
}
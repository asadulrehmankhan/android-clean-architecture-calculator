package io.github.aloussase.calculator.repository

import android.util.Log
import io.github.aloussase.calculator.api.MathApi
import io.github.aloussase.calculator.api.MathApiRequestBody
import io.github.aloussase.calculator.data.HistoryItem
import io.github.aloussase.calculator.database.HistoryDatabase

class CalculatorRepository(
    val mathApi: MathApi,
    val historyDatabase: HistoryDatabase
) {
    suspend fun getAllHistoryItems(): List<HistoryItem> {
        return historyDatabase.historyItemDao().findAll()
    }

    suspend fun createHistoryItem(item: HistoryItem) {
        historyDatabase.historyItemDao().insertOne(item)
    }

    suspend fun calculate(expr: String): Int {
        Log.d("CalculatorRepository", "Calculating expression: $expr")

        val result = mathApi.calculateExpr(
            MathApiRequestBody(
                expr = expr
            )
        )

        if (result.error != null) {
            Log.d("CalculatorRepository", "Failed to get result: ${result.error}")
            return -1
        }

        return result.result?.toInt()!!
    }
}
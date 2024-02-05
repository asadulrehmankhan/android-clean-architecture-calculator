package io.github.aloussase.calculator.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface MathApi {
    @POST
    suspend fun calculateExpr(
        @Body body: MathApiRequestBody,
        @Url url: String = ""
    ): MathApiResult
}
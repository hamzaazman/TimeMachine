package com.hamzaazman.timemachine.data.source.remote

import com.hamzaazman.timemachine.data.model.OnThisDayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WikimediaApiService {

    @GET("feed/v1/wikipedia/{language}/onthisday/{type}/{month}/{day}")
    suspend fun getOnThisDayEvents(
        @Path("language") language: String = "tr",
        @Path("type") type: String = "all",
        @Path("month") month: String,
        @Path("day") day: String
    ): Response<OnThisDayResponse>
}
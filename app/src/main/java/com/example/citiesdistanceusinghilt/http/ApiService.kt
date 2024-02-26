package com.example.citiesdistanceusinghilt.http

import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.MessageResponse
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("https://api.codebazan.ir/distance/index.php")
    suspend fun getDistance(
        @Query("mabda") mabda: String,
        @Query("maghsad") maghsad: String
    ): JsonElement

    @POST("CitiesDistance/Distance/saveDistance.php")
    suspend fun sendDistance(
        @Query("beginning") beginning: String,
        @Query("destination") destination: String,
        @Query("distance") distance: Int
    ): JsonElement

    @GET("CitiesDistance/Distance/getDistanceList.php")
    suspend fun getDistanceList(): List<Distance>

    @GET("CitiesDistance/Distance/getDistanceListCount.php/")
    suspend fun getDistanceCount(): DistanceItemCount

    @POST("CitiesDistance/Distance/deleteDistance.php")
    suspend fun deleteDistance(@Query("id") id: Int): MessageResponse
}

fun createApiServiceInstance(): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://myhostforever.ir/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}
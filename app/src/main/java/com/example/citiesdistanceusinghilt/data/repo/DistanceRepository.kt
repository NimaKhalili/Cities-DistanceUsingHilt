package com.example.citiesdistanceusinghilt.data.repo

import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.MessageResponse
import com.google.gson.JsonElement

interface DistanceRepository {

    suspend fun getDistance(mabda: String, maghsad: String): JsonElement

    suspend fun sendDistance(
        beginning: String,
        destination: String,
        distance: JsonElement
    ): JsonElement

    suspend fun getDistanceList(): List<Distance>
    suspend fun getDistanceCount(): DistanceItemCount
    suspend fun deleteDistance(id: Int): MessageResponse
}
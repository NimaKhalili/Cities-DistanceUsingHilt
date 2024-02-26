package com.example.citiesdistanceusinghilt.data.repo.source

import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.MessageResponse
import com.example.citiesdistanceusinghilt.http.ApiService
import com.google.gson.JsonElement

class DistanceRemoteDataSource(val apiService: ApiService) : DistanceDataSource {
    override suspend fun getDistance(mabda: String, maghsad: String): JsonElement =
        apiService.getDistance(mabda, maghsad)

    override suspend fun sendDistance(beginning: String, destination: String, distance: JsonElement) =
        apiService.sendDistance(beginning, destination, distance.asInt)

    override suspend fun getDistanceList(): List<Distance> = apiService.getDistanceList()

    override suspend fun getDistanceCount(): DistanceItemCount = apiService.getDistanceCount()

    override suspend fun deleteDistance(id: Int): MessageResponse = apiService.deleteDistance(id)
}
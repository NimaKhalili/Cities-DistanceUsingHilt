package com.example.citiesdistanceusinghilt.data.repo.source

import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.MessageResponse
import com.google.gson.JsonElement

class DistanceLocalDataSource : DistanceDataSource {
    override suspend fun getDistance(mabda: String, maghsad: String): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun sendDistance(beginning: String, destination: String, distance: JsonElement): JsonElement {
        TODO("Not yet implemented")
    }

    override suspend fun getDistanceList(): List<Distance> {
        TODO("Not yet implemented")
    }

    override suspend fun getDistanceCount(): DistanceItemCount {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDistance(id: Int): MessageResponse {
        TODO("Not yet implemented")
    }
}
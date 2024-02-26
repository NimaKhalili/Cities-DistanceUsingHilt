package com.example.citiesdistanceusinghilt.data.repo

import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.MessageResponse
import com.example.citiesdistanceusinghilt.data.repo.source.DistanceDataSource
import com.example.citiesdistanceusinghilt.data.repo.source.DistanceLocalDataSource
import com.google.gson.JsonElement

class DistanceRepositoryImpl(
    val remoteDataSource: DistanceDataSource,
    val localDataSource: DistanceLocalDataSource
) : DistanceRepository {
    override suspend fun getDistance(mabda: String, maghsad: String): JsonElement =
        remoteDataSource.getDistance(mabda, maghsad)

    override suspend fun sendDistance(beginning: String, destination: String, distance: JsonElement) =
        remoteDataSource.sendDistance(beginning, destination, distance)

    override suspend fun getDistanceList(): List<Distance> = remoteDataSource.getDistanceList()

    override suspend fun getDistanceCount(): DistanceItemCount = remoteDataSource.getDistanceCount()

    override suspend fun deleteDistance(id: Int): MessageResponse = remoteDataSource.deleteDistance(id)
}
package com.example.citiesdistanceusinghilt.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.citiesdistanceusinghilt.common.BaseViewModel
import com.example.citiesdistanceusinghilt.common.Event
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.repo.DistanceRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class HomeViewModel(private val distanceRepository: DistanceRepository) : BaseViewModel() {
    private val _distanceLiveData = MutableLiveData<JsonElement>()
    val distanceLiveData: LiveData<JsonElement>
        get() = _distanceLiveData

    fun getDistance(beginning: String, destination: String) {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            progressDialogLiveData.value = true
            val result = distanceRepository.getDistance(beginning, destination)
            _distanceLiveData.value = result
            sendDistanceToServer(beginning, destination, result)
            progressDialogLiveData.value = false
        }
    }

    private suspend fun sendDistanceToServer(beginning: String, destination: String, distance: JsonElement) {
        val response = distanceRepository.sendDistance(beginning, destination, distance)
        if (response.asString == "SUCCESS") {
            refreshBadgeCount()
            snackBarLiveData.value = Event("با موفقیت اضافه شد")
        } else if (response.asString == "FAILED")
            snackBarLiveData.value = Event("مشکلی در اضافه کردن پیش آمده")
    }

    private fun refreshBadgeCount() {
        val distanceItemCount =
            EventBus.getDefault().getStickyEvent(DistanceItemCount::class.java)
        distanceItemCount?.let {
            it.count += 1
            EventBus.getDefault().postSticky(it)
        }
    }
}
package com.example.citiesdistance.feature.distance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.citiesdistanceusinghilt.R
import com.example.citiesdistanceusinghilt.common.BaseViewModel
import com.example.citiesdistanceusinghilt.common.Event
import com.example.citiesdistanceusinghilt.data.Distance
import com.example.citiesdistanceusinghilt.data.DistanceItemCount
import com.example.citiesdistanceusinghilt.data.EmptyState
import com.example.citiesdistanceusinghilt.data.MessageResponse
import com.example.citiesdistanceusinghilt.data.repo.DistanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class DistanceViewModel(private val distanceRepository: DistanceRepository) : BaseViewModel() {

    private val _emptyStateLiveData = MutableLiveData<EmptyState>()
    val emptyStateLiveData: LiveData<EmptyState>
        get() = _emptyStateLiveData
    private val _distanceLiveData = MutableLiveData<List<Distance>>()
    val distanceLiveData: LiveData<List<Distance>>
        get() = _distanceLiveData

    init {
        getDistanceList()
    }

    fun refreshList() {
        getDistanceList()
    }

    private fun getDistanceList() {
        viewModelScope.launch(Dispatchers.Main + coroutineExceptionHandler) {
            progressDialogLiveData.value = true
            val distanceList = distanceRepository.getDistanceList()
            if (distanceList.isNotEmpty()) {
                _distanceLiveData.value = distanceList
                _emptyStateLiveData.value = EmptyState(false)
            } else {
                _emptyStateLiveData.value =
                    EmptyState(true, R.string.distance_default_empty_state, true)
            }
            progressDialogLiveData.value = false
        }
    }

    fun deleteDistance(distance: Distance) {
        viewModelScope.launch(Dispatchers.Main) {
            progressDialogLiveData.value = true
            val messageResponse = distanceRepository.deleteDistance(distance.id)
            prepareResponse(messageResponse, distance)
            progressDialogLiveData.value = false
        }
    }

    private fun prepareResponse(t: MessageResponse, distance: Distance) {
        if (t.response == "SUCCESS") {
            refreshLiveData(distance)
            refreshBadgeCount()
            checkListToShowEmptyState()
            snackBarLiveData.value = Event("با موفقیت حذف شد")
        } else if (t.response == "FAILED") {
            snackBarLiveData.value = Event("حذف ناموفق بود")
        }
    }

    private fun checkListToShowEmptyState() {
        _distanceLiveData.value?.let {
            if (it.isEmpty())
                _emptyStateLiveData.value = EmptyState(true, R.string.distance_finished_empty_state)
        }
    }

    private fun refreshBadgeCount() {
        val distanceItemCount =
            EventBus.getDefault().getStickyEvent(DistanceItemCount::class.java)
        distanceItemCount?.let {
            it.count -= 1
            EventBus.getDefault().postSticky(it)
        }
    }

    private fun refreshLiveData(distance: Distance) {
        _distanceLiveData.value = _distanceLiveData.value?.toMutableList()?.apply {
            remove(distance)
        }
    }
}
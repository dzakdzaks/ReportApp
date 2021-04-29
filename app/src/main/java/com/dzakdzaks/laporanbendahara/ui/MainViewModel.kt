package com.dzakdzaks.laporanbendahara.ui

import androidx.lifecycle.*
import com.dzakdzaks.laporanbendahara.data.MainRepository
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    private val _shouldLoadReports = MutableLiveData<Boolean>()
    val reports: LiveData<Resource<List<Report>>> = _shouldLoadReports.switchMap {
        mainRepository.getReports().asLiveData()
    }

    private val _shouldLaunchAddReport = SingleLiveEvent<Boolean>()
    val shouldLaunchAddReport: LiveData<Boolean> = _shouldLaunchAddReport

    init {
        _shouldLoadReports.value = true
    }

    fun onAddReportClicked() {
        Timber.d("onAddNameClicked: Add name clicked ")
        _shouldLaunchAddReport.value = true
    }

    fun onRefreshClicked() {
        Timber.d("onRefreshClicked: On refresh clicked")
        _shouldLoadReports.value = true
    }

    fun onNewReportAdded() {
        _shouldLoadReports.value = true
    }

}
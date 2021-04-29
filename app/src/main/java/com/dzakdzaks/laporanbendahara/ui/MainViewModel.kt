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

    val reportsData: MutableList<Report> = mutableListOf()

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

    fun customDataHeader(responseListReport: List<Report>): List<Report> {
        val resultList: MutableList<Report> = mutableListOf()
        var prevDate = ""
        reportsData.let {
            if (!it.isNullOrEmpty()) {
                prevDate = it[it.size - 1].getCreatedAtJustDate()
            }
        }
        responseListReport.forEach { item ->
            val currDate = item.getCreatedAtJustDate()
            if (prevDate != currDate) {
                prevDate = currDate
                resultList.add(Report(itemType = Report.ITEM_HEADER, createdAt = item.createdAt))
            }
            resultList.add(item)
        }
        return resultList
    }

}
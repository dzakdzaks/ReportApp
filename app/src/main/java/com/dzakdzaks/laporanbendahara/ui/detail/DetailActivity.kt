package com.dzakdzaks.laporanbendahara.ui.detail

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.databinding.ActivityDetailBinding
import com.dzakdzaks.laporanbendahara.utils.extension.convertDateToReadable
import com.dzakdzaks.laporanbendahara.utils.extension.showMaterialDatePicker
import com.dzakdzaks.laporanbendahara.utils.extension.startIntent
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_detail
        )
    }

    private val viewModel: DetailViewModel by viewModels()

    private var selectedTimeInMillis = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding.apply {

            inputDate.editText?.apply {
                setText(Date(System.currentTimeMillis()).convertDateToReadable())
                setOnClickListener {
                    showMaterialDatePicker(true, selectedTimeInMillis) {
                        selectedTimeInMillis = it
                        val selectedDateString = Date(it).convertDateToReadable()
                        inputDate.editText?.setText(selectedDateString)
                    }
                }
            }
            val adapter = ArrayAdapter(this@DetailActivity, R.layout.item_auto_complete, viewModel.getSourceIncome())
            (inputTitle.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    companion object {

        private const val REPORT = "report"
        private const val TYPE = "type"

        const val ADD = "add"
        const val DETAIL = "detail"

        fun newInstance(activity: Activity, report: Report?, type: String) {
            activity.startIntent(DetailActivity::class.java) {
                if (report != null) it.putExtra(REPORT, report)
                it.putExtra(TYPE, type)
            }
        }
    }
}
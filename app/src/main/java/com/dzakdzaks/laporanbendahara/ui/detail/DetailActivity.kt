package com.dzakdzaks.laporanbendahara.ui.detail

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.databinding.ActivityDetailBinding
import com.dzakdzaks.laporanbendahara.utils.extension.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        binding.vm = viewModel

        intent?.getStringExtra(ACTION)?.let {
            if (it == ADD) {

            } else {
                setupDetail()
            }
        }

        binding.fab.setOnClickListener {

        }
    }

    private fun setupDetail() {
        binding.apply {
            intent.getParcelableExtra<Report>(REPORT)?.let {

                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setDisplayShowHomeEnabled(true)
                    title = if (it.type == Report.INCOME) "Pemasukan" else "Pengeluaran"

                }

                inputDate.apply {
                    hint = if (it.type == Report.INCOME) "Tanggal Pemasukan" else "Tanggal Pengeluaran"
                    editText?.setOnClickListener {
                        showMaterialDatePicker(true, selectedTimeInMillis) { dateLong ->
                            selectedTimeInMillis = dateLong
                            val selectedDateString = Date(dateLong).convertDateToReadable()
                            viewModel.date.value = selectedDateString
                        }
                    }
                }

                inputTitle.apply {
                    hint = if (it.type == Report.INCOME) "Sumber Pemasukan" else "Jenis Pengeluaran"
                    val adapter = ArrayAdapter(
                            this@DetailActivity,
                            R.layout.item_auto_complete,
                            if (it.type == Report.INCOME) viewModel.getSourceIncome() else viewModel.getTypeExpense()
                    )
                    (editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }

                inputTotal.apply {
                    hint = if (it.type == Report.INCOME) "Jumlah Pemasukan" else "Jumlah Pengeluaran"

                }

                tvTitleCheckbox.text = if (it.type == Report.INCOME) "Penerima & Saksi" else "Yang Mengeluarkan"

                if (it.type == Report.INCOME) {
                    inputReceiver.gone()
                    inputWitness.gone()
                } else {
                    inputReceiver.visible()
                    inputWitness.visible()
                }

                generateCheckBox(it)
            }
        }
    }

    private fun generateCheckBox(report: Report) {

    }

    companion object {

        private const val REPORT = "report"

        private const val ACTION = "action"
        const val ADD = "add"
        const val DETAIL = "detail"

        fun newInstance(activity: Activity, report: Report?, action: String) {
            activity.startIntent(DetailActivity::class.java) {
                if (report != null) it.putExtra(REPORT, report)
                it.putExtra(ACTION, action)
            }
        }
    }
}
package com.dzakdzaks.laporanbendahara.ui.detail

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
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
                setupAdd()
            } else {
                setupDetail()
            }
        }

        binding.fab.setOnClickListener {

        }
    }

    private fun setupAdd() {
        binding.apply {
            containerType.visible()

            conditionalViewByIsChecked(toggle.checkedRadioButtonId)
            toggle.setOnCheckedChangeListener { _, checkedId -> conditionalViewByIsChecked(checkedId) }
        }
    }

    private fun setupDetail() {
        binding.apply {
            containerType.gone()
            intent.getParcelableExtra<Report>(REPORT)?.let {

                conditionalViewByReport(report = it)

                tvTitleCheckbox.text = if (it.type == Report.INCOME) "Penerima & Saksi" else "Yang Mengeluarkan"

                if (it.type == Report.INCOME) {
                    inputReceiver.gone()
                    inputWitness.gone()
                } else {
                    inputReceiver.visible()
                    inputWitness.visible()
                }

            }
        }
    }

    private fun conditionalViewByIsChecked(@IdRes checkedId: Int ) {
        binding.apply {
            val isExpense = checkedId == R.id.rbExpense
            if (isExpense) {
                inputReceiver.visible()
                inputWitness.visible()
            } else {
                inputReceiver.gone()
                inputWitness.gone()
            }

            tvTitleCheckbox.text = if (isExpense) "Yang Mengeluarkan" else "Penerima & Saksi"

            inputDate.apply {
                hint = if (isExpense) "Tanggal Pengeluaran" else "Tanggal Pemasukan"
                editText?.setOnClickListener {
                    showMaterialDatePicker(true, selectedTimeInMillis) { dateLong ->
                        selectedTimeInMillis = dateLong
                        val selectedDateString = Date(dateLong).convertDateToReadable()
                        viewModel.date.value = selectedDateString
                    }
                }
            }

            inputTitle.apply {
                hint = if (isExpense) "Jenis Pengeluaran" else "Sumber Pemasukan"
                val adapter = ArrayAdapter(
                        this@DetailActivity,
                        R.layout.item_auto_complete,
                        if (isExpense)  viewModel.getTypeExpense() else viewModel.getSourceIncome()
                )
                (editText as? AutoCompleteTextView)?.setAdapter(adapter)
            }

            inputTotal.apply {
                hint = if (isExpense) "Jumlah Pengeluaran" else  "Jumlah Pemasukan"

            }
        }
    }

    private fun conditionalViewByReport(report: Report) {
        binding.apply {

            supportActionBar?.title = if (report.type == Report.INCOME) "Pemasukan" else "Pengeluaran"

            inputDate.apply {
                hint = if (report.type == Report.INCOME) "Tanggal Pemasukan" else "Tanggal Pengeluaran"
                editText?.setOnClickListener {
                    showMaterialDatePicker(true, selectedTimeInMillis) { dateLong ->
                        selectedTimeInMillis = dateLong
                        val selectedDateString = Date(dateLong).convertDateToReadable()
                        viewModel.date.value = selectedDateString
                    }
                }
            }

            inputTitle.apply {
                hint = if (report.type == Report.INCOME) "Sumber Pemasukan" else "Jenis Pengeluaran"
                val adapter = ArrayAdapter(
                        this@DetailActivity,
                        R.layout.item_auto_complete,
                        if (report.type == Report.INCOME) viewModel.getSourceIncome() else viewModel.getTypeExpense()
                )
                (editText as? AutoCompleteTextView)?.setAdapter(adapter)
            }

            inputTotal.apply {
                hint = if (report.type == Report.INCOME) "Jumlah Pemasukan" else "Jumlah Pengeluaran"

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
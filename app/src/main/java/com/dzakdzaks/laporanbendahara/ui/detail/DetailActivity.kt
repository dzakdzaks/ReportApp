package com.dzakdzaks.laporanbendahara.ui.detail

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.databinding.ActivityDetailBinding
import com.dzakdzaks.laporanbendahara.utils.extension.*
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

        intent?.apply {
            getStringExtra(ACTION)?.let {
                if (it == ADD) {
                    setupAdd()
                } else {
                    setupDetail()
                }
            }

            getIntExtra(COUNT, 0).let {
                viewModel.countData = it
            }

        }

        binding.fab.setOnClickListener {
            viewModel.addReport()
        }
    }

    /**=============================DETAIL REPORT=================================*/

    private fun setupDetail() {
        binding.apply {
            containerType.gone()
            intent.getParcelableExtra<Report>(REPORT)?.let {

                conditionalViewByReport(report = it)

                tvTitleCheckbox.text = if (it.type == Report.INCOME) "Penerima & Saksi" else "Yang Mengeluarkan"

                generateCheckBoxByReport(it)

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

    private fun conditionalViewByReport(report: Report) {
        binding.apply {

            supportActionBar?.title = if (report.type == Report.INCOME) "Laporan Pemasukan" else "Laporan Pengeluaran"

            inputDate.apply {
                hint = if (report.type == Report.INCOME) "Tanggal Pemasukan" else "Tanggal Pengeluaran"
                editText?.setOnClickListener {
                    showMaterialDatePicker(true, viewModel.selectedTimeInMillis) { dateLong ->
                        viewModel.selectedTimeInMillis = dateLong
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

    private fun generateCheckBoxByReport(report: Report) {
        if (report.type == Report.INCOME) {
            viewModel.getListReceiverAndWitness().forEach { createCheckBox(it) }
        } else {
            viewModel.getListWhoExpense().forEach { createCheckBox(it) }
        }
    }

    /**=============================FINISH DETAIL REPORT=================================*/


    /**=============================ADD REPORT=================================*/

    private fun setupAdd() {
        binding.apply {
            containerType.visible()

            conditionalViewByIsChecked(toggle.checkedRadioButtonId)
            toggle.setOnCheckedChangeListener { _, checkedId -> conditionalViewByIsChecked(checkedId) }
        }
    }

    private fun conditionalViewByIsChecked(@IdRes checkedId: Int) {
        binding.apply {
            viewModel.checkBoxValues.clear()
            inputOther.gone()

            supportActionBar?.title = "Buat Laporan"

            val isExpense = checkedId == R.id.rbExpense
            if (isExpense) {
                inputReceiver.visible()
                inputWitness.visible()
            } else {
                inputReceiver.gone()
                inputWitness.gone()
            }

            viewModel.typeReport = if (isExpense) Report.EXPENSE else Report.INCOME

            tvTitleCheckbox.text = if (isExpense) "Yang Mengeluarkan" else "Penerima & Saksi"

            generateCheckBoxByType(isExpense)

            inputDate.apply {
                hint = if (isExpense) "Tanggal Pengeluaran" else "Tanggal Pemasukan"
                editText?.setOnClickListener {
                    showMaterialDatePicker(true, viewModel.selectedTimeInMillis) { dateLong ->
                        viewModel.selectedTimeInMillis = dateLong
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
                        if (isExpense) viewModel.getTypeExpense() else viewModel.getSourceIncome()
                )
                (editText as? AutoCompleteTextView)?.setAdapter(adapter)
            }

            inputTotal.apply {
                hint = if (isExpense) "Jumlah Pengeluaran" else "Jumlah Pemasukan"

            }
        }
    }

    private fun generateCheckBoxByType(isExpense: Boolean) {
        binding.apply {
            if (isExpense) {
                if (linearCheckbox.childCount > 1) linearCheckbox.removeViews(1, viewModel.getListReceiverAndWitness().count())
                viewModel.getListWhoExpense().forEach { createCheckBox(it) }
            } else {
                if (linearCheckbox.childCount > 1) linearCheckbox.removeViews(1, viewModel.getListWhoExpense().count())
                viewModel.getListReceiverAndWitness().forEach { createCheckBox(it) }
            }
        }
    }

    /**=============================FINISH ADD REPORT=================================*/


    private fun createCheckBox(value: String) {
        val checkBox = CheckBox(this)
        checkBox.text = value
        checkBox.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        checkBox.setOnCheckedChangeListener { cb, isChecked ->
            val title = cb.text.toString()
            if (isChecked) viewModel.checkBoxValues.add(title) else viewModel.checkBoxValues.remove(title)
            if (viewModel.checkBoxValues.contains("Other")) binding.inputOther.visible() else binding.inputOther.gone()
        }
        binding.linearCheckbox.addView(checkBox)
    }

    companion object {

        private const val REPORT = "report"

        private const val ACTION = "action"
        const val ADD = "add"
        const val DETAIL = "detail"

        private const val COUNT = "count"

        fun newInstance(activity: Activity, report: Report?, action: String, count: Int) {
            activity.startIntent(DetailActivity::class.java) {
                if (report != null) it.putExtra(REPORT, report)
                it.putExtra(ACTION, action)
                it.putExtra(COUNT, count)
            }
        }
    }
}

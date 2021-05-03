package com.dzakdzaks.laporanbendahara.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.databinding.ActivityDetailBinding
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.extension.*
import com.google.android.material.checkbox.MaterialCheckBox
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        intent.getStringExtra(ACTION)?.let {
            return if (it == DETAIL) {
                menuInflater.inflate(R.menu.menu_detail, menu)
                true
            } else {
                false
            }
        } ?: return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_delete -> { viewModel.deleteReport() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewModel.isAfterUpdate) {
            setResult(RESULT_OK)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupView() {
        binding.vm = viewModel

        intent?.apply {
            getStringExtra(ACTION)?.let {
                if (it == ADD) {
                    setupAdd()
                    observeData(ADD)
                } else {
                    setupDetail()
                    observeData(DETAIL)
                }
            }

            getIntExtra(COUNT, 0).let {
                viewModel.countData = it
            }

        }
    }

    /**=============================DETAIL REPORT=================================*/

    private fun setupDetail() {
        binding.apply {
            fab.setImageResource(R.drawable.ic_check)
            rbIncome.disable()
            rbExpense.disable()
            intent.getParcelableExtra<Report>(REPORT)?.let {
                viewModel.report = it
                conditionalViewByReport(report = it)
                generateCheckBoxByReport(it)

                binding.fab.setOnClickListener {
                    hideSoftKeyBoard()
                    viewModel.updateReport()
                }
            }
        }
    }

    private fun conditionalViewByReport(report: Report) {
        binding.apply {

            if (report.type == Report.INCOME) {
                inputReceiver.gone()
                inputWitness.gone()
            } else {
                inputReceiver.visible()
                inputWitness.visible()
            }

            supportActionBar?.title =
                if (report.type == Report.INCOME) "Laporan Pemasukan" else "Laporan Pengeluaran"

            tvTitleCheckbox.text =
                if (report.type == Report.INCOME) "Penerima & Saksi" else "Yang Mengeluarkan"

            inputDate.apply {
                hint =
                    if (report.type == Report.INCOME) "Tanggal Pemasukan" else "Tanggal Pengeluaran"
                editText?.setOnClickListener {
                    showMaterialDatePicker(true, viewModel.selectedTimeInMillis) { dateLong ->
                        viewModel.selectedTimeInMillis = dateLong
                        val selectedDateString = Date(dateLong).convertDateToReadable()
                        editText?.setText(selectedDateString)
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
                (editText as? AutoCompleteTextView)?.apply {
                    setAdapter(adapter)
                    doOnTextChanged { text, _, _, _ ->
                        viewModel.title.value = text.toString()
                    }
                }
            }

            inputTotal.apply {
                hint =
                    if (report.type == Report.INCOME) "Jumlah Pemasukan" else "Jumlah Pengeluaran"
            }

            /*set data*/
            if (report.type == Report.INCOME) {
                toggle.check(R.id.rbIncome)
                viewModel.apply {
                    date.value = report.dateIncome.getFullDateTimeJustDateReadable()
                    (inputTitle.editText as? AutoCompleteTextView)?.setText(
                        report.sourceIncome,
                        false
                    )
                    total.value = report.totalIncome
                    desc.value = report.descriptionIncome
                }
                /*inputDate.editText?.setText(report.dateIncome.getFullDateTimeJustDateReadable())
                inputTitle.editText?.setText(report.sourceIncome)
                inputTotal.editText?.setText(report.totalIncome)
                inputDesc.editText?.setText(report.descriptionIncome)*/

            } else {
                toggle.check(R.id.rbExpense)
                viewModel.apply {
                    date.value = report.dateExpense.getFullDateTimeJustDateReadable()
                    (inputTitle.editText as? AutoCompleteTextView)?.setText(
                        report.typeExpense,
                        false
                    )
                    total.value = report.totalExpense
                    receiver.value = report.whoReceived
                    witness.value = report.witnessExpense
                    desc.value = report.descriptionExpense
                }
                /*inputDate.editText?.setText(report.dateExpense.getFullDateTimeJustDateReadable())
                inputTitle.editText?.setText(report.typeExpense)
                inputTotal.editText?.setText(report.totalExpense)
                inputReceiver.editText?.setText(report.whoReceived)
                inputWitness.editText?.setText(report.witnessExpense)
                inputDesc.editText?.setText(report.descriptionExpense)*/
            }
            /*set data*/
        }
    }

    private fun generateCheckBoxByReport(report: Report) {
        if (report.type == Report.INCOME) {
            viewModel.getListReceiverAndWitness()
                .forEach { viewModel.checkboxes.add(createCheckBox(it)) }
        } else {
            viewModel.getListWhoExpense().forEach { viewModel.checkboxes.add(createCheckBox(it)) }
        }
        setCheckedCheckBox(report)
    }

    private fun setCheckedCheckBox(report: Report) {
        val listData =
            (if (report.type == Report.INCOME) report.recipientAndWitnessIncome
            else report.whoExpense).split(", ").filter { it != "" }

        val listCBText = mutableListOf<String>()
        viewModel.checkboxes.forEach {
            listCBText.add(it.text.toString())
        }

        viewModel.checkboxes.forEach { checkBox ->

            val listOther = mutableListOf<String>()

            listData.forEach { value ->
                if (value == checkBox.text) {
                    checkBox.isChecked = true
                } else {
                    if (checkBox.text == "Other" && !listCBText.contains(value)) {
                        checkBox.isChecked = true
                        listOther.add(value)
                    }
                }
            }

            if (!listOther.isNullOrEmpty()) {
                viewModel.other.value = listOther.joinToString()
            }
        }
    }

    /**=============================FINISH DETAIL REPORT=================================*/


    /**=============================ADD REPORT=================================*/

    private fun setupAdd() {
        binding.apply {
            fab.setImageResource(R.drawable.ic_add)
            containerType.visible()
            supportActionBar?.title = "Buat Laporan"
            conditionalViewByIsChecked(toggle.checkedRadioButtonId)
            toggle.setOnCheckedChangeListener { _, checkedId -> conditionalViewByIsChecked(checkedId) }
            binding.fab.setOnClickListener {
                hideSoftKeyBoard()
                viewModel.addReport()
            }
        }
    }

    private fun conditionalViewByIsChecked(@IdRes checkedId: Int) {
        binding.apply {

            resetData()

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
                        editText?.setText(selectedDateString)
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
                (editText as? AutoCompleteTextView)?.apply {
                    setAdapter(adapter)
                    doOnTextChanged { text, _, _, _ ->
                        viewModel.title.value = text.toString()
                    }
                }
            }

            inputTotal.apply {
                hint = if (isExpense) "Jumlah Pengeluaran" else "Jumlah Pemasukan"

            }
        }
    }

    private fun generateCheckBoxByType(isExpense: Boolean) {
        binding.apply {
            if (isExpense) {
                if (linearCheckbox.childCount > 1) linearCheckbox.removeViews(
                    1,
                    viewModel.getListReceiverAndWitness().count()
                )
                viewModel.getListWhoExpense().forEach { createCheckBox(it) }
            } else {
                if (linearCheckbox.childCount > 1) linearCheckbox.removeViews(
                    1,
                    viewModel.getListWhoExpense().count()
                )
                viewModel.getListReceiverAndWitness().forEach { createCheckBox(it) }
            }
        }
    }

    private fun resetData() {
        binding.apply {
            viewModel.checkboxes.clear()
            inputOther.gone()
            parent.requestFocus()
            hideSoftKeyBoard()
            inputDate.editText?.setText("")
            inputTitle.editText?.setText("")
            inputTotal.editText?.setText("")
            inputOther.editText?.setText("")
            inputReceiver.editText?.setText("")
            inputWitness.editText?.setText("")
            inputDesc.editText?.setText("")
        }
    }

    /**=============================FINISH ADD REPORT=================================*/


    private fun createCheckBox(value: String): MaterialCheckBox {
        val checkBox = MaterialCheckBox(this)
        checkBox.text = value
        checkBox.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        checkBox.setOnCheckedChangeListener { cb, isChecked ->
            val title = cb.text.toString()
            if (isChecked) viewModel.checkBoxesValue.add(title) else viewModel.checkBoxesValue.remove(
                title
            )

            val otherCheck = viewModel.checkBoxesValue.find { it == "Other" }
            otherCheck?.let {
                binding.inputOther.visible()
            } ?: binding.inputOther.gone()
        }
        binding.linearCheckbox.addView(checkBox)
        return checkBox
    }

    private fun observeData(action: String) {
        when (action) {
            ADD -> {
                viewModel.addReportResponse.observe(this, {
                    when (it) {

                        is Resource.Loading -> {
                            binding.apply {
                                if (it.isLoading) fab.disable() else fab.enable()
                                frameLoading.toggleLoading(isLoading = it.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            Toast.makeText(this, "Laporan berhasil dibuat", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        }

                        is Resource.Error -> {
                            Timber.d("wakwaw ${it.errorData}")
                            Toast.makeText(this, it.errorData, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
            }
            DETAIL -> {
                viewModel.updateReportResponse.observe(this, {
                    when (it) {

                        is Resource.Loading -> {
                            binding.apply {
                                if (it.isLoading) fab.disable() else fab.enable()
                                frameLoading.toggleLoading(isLoading = it.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            viewModel.isAfterUpdate = true
                            Toast.makeText(this, "Laporan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        }

                        is Resource.Error -> {
                            Timber.d("wakwaw ${it.errorData}")
                            Toast.makeText(this, it.errorData, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                viewModel.deleteReportResponse.observe(this, {
                    when (it) {

                        is Resource.Loading -> {
                            binding.apply {
                                if (it.isLoading) fab.disable() else fab.enable()
                                frameLoading.toggleLoading(isLoading = it.isLoading)
                            }
                        }

                        is Resource.Success -> {
                            Toast.makeText(this, "Laporan berhasil di hapus", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        }

                        is Resource.Error -> {
                            Timber.d("wakwaw ${it.errorData}")
                            Toast.makeText(this, it.errorData, Toast.LENGTH_SHORT).show();
                        }
                    }
                })

            }
        }

    }

    companion object {

        private const val REPORT = "report"

        private const val ACTION = "action"
        const val ADD = "add"
        const val DETAIL = "detail"

        private const val COUNT = "count"

        fun newInstanceResult(
            activity: Activity,
            report: Report?,
            action: String,
            count: Int
        ): Intent =
            activity.startIntentResult(DetailActivity::class.java) {
                if (report != null) it.putExtra(REPORT, report)
                it.putExtra(ACTION, action)
                it.putExtra(COUNT, count)
            }
    }
}

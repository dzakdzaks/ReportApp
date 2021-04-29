package com.dzakdzaks.laporanbendahara.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.databinding.ActivityMainBinding
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.extension.toggleLoading
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
            )

        binding.vm = viewModel

        setupAdapter(binding)
        observeData(binding)
    }

    private fun setupAdapter(binding: ActivityMainBinding) {
        mainAdapter = MainAdapter(null) {
            Toast.makeText(this, it.type, Toast.LENGTH_SHORT).show()
        }
        binding.list.apply {
            adapter = mainAdapter
            addItemDecoration(
                PinnedHeaderItemDecoration.Builder(Report.ITEM_HEADER)
                    .enableDivider(false)
                    .disableHeaderClick(true)
                    .create()
            )
        }

    }

    private fun observeData(binding: ActivityMainBinding) {
        viewModel.reports.observe(this, {
            when (it) {

                is Resource.Loading -> {
                    binding.progressBar.toggleLoading(isLoading = it.isLoading)
                }

                is Resource.Success -> {
                    viewModel.reportsData.addAll(viewModel.customDataHeader(it.data))
                    mainAdapter.setNewInstance(viewModel.reportsData)
                }

                is Resource.Error -> {
                    Timber.d("wakwaw ${it.errorData}")
                    Toast.makeText(this, it.errorData, Toast.LENGTH_SHORT).show();
                }
            }
        })

    }
}
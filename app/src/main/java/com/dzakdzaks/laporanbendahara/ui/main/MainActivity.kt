package com.dzakdzaks.laporanbendahara.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.data.remote.model.Report
import com.dzakdzaks.laporanbendahara.databinding.ActivityMainBinding
import com.dzakdzaks.laporanbendahara.ui.detail.DetailActivity
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.dzakdzaks.laporanbendahara.utils.extension.startIntent
import com.dzakdzaks.laporanbendahara.utils.extension.toggleLoading
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(
                this,
                R.layout.activity_main
        )
    }
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupAdapter()
        setupClicked()
        observeData()
    }

    private fun setupAdapter() {
        mainAdapter = MainAdapter(null) {
            DetailActivity.newInstance(this, it, DetailActivity.DETAIL, mainAdapter.getCountBody())
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

    private fun setupClicked() {
        binding.apply {
            fab.setOnClickListener {
                DetailActivity.newInstance(this@MainActivity, null, DetailActivity.ADD, mainAdapter.getCountBody())
            }
        }
    }

    private fun observeData() {
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
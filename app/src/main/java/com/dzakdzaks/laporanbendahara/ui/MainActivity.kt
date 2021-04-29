package com.dzakdzaks.laporanbendahara.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dzakdzaks.laporanbendahara.R
import com.dzakdzaks.laporanbendahara.databinding.ActivityMainBinding
import com.dzakdzaks.laporanbendahara.utils.Resource
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(
                this,
                R.layout.activity_main
            )

        binding.vm = viewModel

        observeData(binding)
    }

    private fun observeData(binding: ActivityMainBinding) {
        viewModel.reports.observe(this, {
            when (it) {

                is Resource.Loading -> {
                    Timber.d("wakwaw ${it.isLoading}")
                }

                is Resource.Success -> {
                    Timber.d("wakwaw ${it.data.size}")
                }

                is Resource.Error -> {
                    Timber.d("wakwaw ${it.errorData}")
                    Toast.makeText(this, it.errorData, Toast.LENGTH_SHORT).show();
                }
            }
        })

    }
}
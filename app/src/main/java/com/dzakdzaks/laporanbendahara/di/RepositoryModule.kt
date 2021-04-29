package com.dzakdzaks.laporanbendahara.di

import com.dzakdzaks.laporanbendahara.data.MainRepository
import com.dzakdzaks.laporanbendahara.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {


    @Provides
    @ViewModelScoped
    fun provideMainRepository(apiInterface: ApiInterface): MainRepository =
        MainRepository(apiInterface)
}
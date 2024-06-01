package com.example.networkspeedo.di

import com.example.networkspeedo.presentation.settings.SettingsFragment
import com.example.networkspeedo.presentation.speedo.SpeedoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun speedoFragment(): SpeedoFragment

    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment
}
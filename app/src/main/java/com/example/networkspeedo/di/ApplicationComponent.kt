package com.example.networkspeedo.di

import com.example.networkspeedo.TheApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class,
        FragmentModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<TheApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: TheApplication): ApplicationComponent
    }
}
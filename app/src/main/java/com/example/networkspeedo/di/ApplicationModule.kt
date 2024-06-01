package com.example.networkspeedo.di

import android.app.Application
import android.content.Context
import com.example.networkspeedo.TheApplication
import dagger.Module
import dagger.Provides

@Module
open class ApplicationModule {

    @Provides
    fun provideApplicationContext(app: TheApplication): Context {
        return app.applicationContext
    }
}
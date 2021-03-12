package ru.skillbranch.skillarticles

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

class App: Application() {

    companion object{
        private lateinit var instance: App

        fun applicationContext(): Context{
            return instance.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

}
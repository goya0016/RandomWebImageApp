
package com.example.randomwebimageapp

import android.app.Application
import android.content.Context

/*
* Created by Student Name on November 06, 2020
*/
class TheApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        lateinit var context: Context
            private set
    }
}
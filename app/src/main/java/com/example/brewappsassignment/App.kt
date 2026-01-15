package com.example.brewappsassignment

import android.app.Application
import com.example.brewappsassignment.data.local.SessionManager
import com.example.brewappsassignment.data.remote.SupabaseClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SupabaseClient.init(SessionManager(this))
    }
}

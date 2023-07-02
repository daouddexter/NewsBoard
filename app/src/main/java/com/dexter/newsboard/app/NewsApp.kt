package com.dexter.newsboard.app

import android.app.Application
import android.content.Context

class NewsApp : Application() {

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        appContext = this
    }

}
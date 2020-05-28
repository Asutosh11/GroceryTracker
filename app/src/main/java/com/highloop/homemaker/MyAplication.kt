package com.highloop.homemaker

import android.app.Application
import com.highloop.homemaker.data.dao.ItemsListDAO
import com.highloop.homemaker.util.ObjectBoxUtil

class MyAplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBoxUtil.init(this)
    }


}
package `in`.thoughtleaf.grocerytracker

import android.app.Application
import `in`.thoughtleaf.grocerytracker.util.ObjectBoxUtil

class MyAplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBoxUtil.init(this)
    }


}
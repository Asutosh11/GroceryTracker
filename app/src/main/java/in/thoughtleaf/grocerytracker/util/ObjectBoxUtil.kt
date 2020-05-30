package `in`.thoughtleaf.grocerytracker.util

import android.content.Context
import `in`.thoughtleaf.grocerytracker.data.dao.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder

public class ObjectBoxUtil {

    companion object{

        lateinit var boxStore : BoxStore
        lateinit var builder : BoxStoreBuilder

        fun init(context: Context) {

            builder = MyObjectBox.builder()

            boxStore = builder
                .androidContext(context)
                .build()
        }
    }
}
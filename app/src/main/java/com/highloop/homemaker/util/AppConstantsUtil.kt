package com.highloop.homemaker.util

import androidx.annotation.StringDef
import java.lang.annotation.RetentionPolicy


class AppConstantsUtil {

    companion object{

        sealed class AdditionScreenType {
            class AddProduct() : AdditionScreenType()
            class AddCategory() : AdditionScreenType()
        }
    }


}
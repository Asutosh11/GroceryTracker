package `in`.thoughtleaf.grocerytracker.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.thoughtleaf.grocerytracker.R

@SuppressLint("AppCompatCustomView")
class SmallSpinner : Spinner {

    private var mContext: Context? = null

    constructor(context: Context?) : super(context){
        mContext = context
    }

    constructor(context: Context?, mode: Int) : super(context, mode){
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(context,attrs,defStyleAttr,mode){
        init(context, attrs)
    }

    fun init(context: Context?, attrs: AttributeSet?){
        mContext = context
        setBackgroundResource(R.drawable.spinner_box_small)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

}
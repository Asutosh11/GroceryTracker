package `in`.thoughtleaf.grocerytracker.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.Spinner
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatSpinner
import com.thoughtleaf.grocerytracker.R

@SuppressLint("AppCompatCustomView")
class LargeSpinner : Spinner {

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
        setPadding(16, 0, 0, 0)
    }

    override fun onDraw(canvas: Canvas?) {
        // change item 4
        val backgroundBitmap = AppCompatResources.getDrawable(context!!, R.drawable.spinner_box)
        backgroundBitmap?.setBounds(0, 0, width, height)
        backgroundBitmap?.draw(canvas!!)

        super.onDraw(canvas)

    }

}
package `in`.thoughtleaf.grocerytracker.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.thoughtleaf.grocerytracker.R

@SuppressLint("AppCompatCustomView")
class CustomAutoCompleteTextView : AutoCompleteTextView {

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        init(context, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int, popupTheme: Resources.Theme?) : super(context, attrs, defStyleAttr, defStyleRes, popupTheme){
        init(context, attrs)
    }

    constructor(context: Context?) : super(context){
        init(context, null)
    }

    fun init(context: Context?, attrs: AttributeSet?){
        val rightDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_keyboard_voice_black_24dp)

        setError(null)
        setCompoundDrawables(null, null, null, null)
        // change item 3
        setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null)
    }

    override fun onDraw(canvas: Canvas?) {
        // change item 4
        val backgroundBitmap = AppCompatResources.getDrawable(context!!, R.drawable.box_shape)
        backgroundBitmap?.setBounds(0, 0, width, height)
        backgroundBitmap?.draw(canvas!!)

        super.onDraw(canvas)

    }
}
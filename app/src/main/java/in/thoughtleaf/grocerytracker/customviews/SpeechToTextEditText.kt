package `in`.thoughtleaf.grocerytracker.customviews

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.thoughtleaf.grocerytracker.R


@SuppressLint("AppCompatCustomView")
class SpeechToTextEditText : EditText {

    private var mContext: Context? = null

    constructor(context: Context?) : super(context){
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(context, attrs)
    }


    fun init(context: Context?, attrs: AttributeSet?){
        mContext = context

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
package `in`.thoughtleaf.grocerytracker.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.thoughtleaf.grocerytracker.R


@SuppressLint("AppCompatCustomView")
class SpeechToTextEditText : EditText {

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
        // val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.SpeechToTextEditText)
        // val drawableResId = typedArray.getResourceId(R.styleable.SpeechToTextEditText_custom_drawable, -1);
        val rightDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_keyboard_voice_black_24dp)

        setError(null);
        setCompoundDrawables(null, null, null, null);
        setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }



}
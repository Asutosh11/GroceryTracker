package `in`.thoughtleaf.grocerytracker.customviews

import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import com.thoughtleaf.grocerytracker.R


@SuppressLint("AppCompatCustomView")
class DbAttachedEditText : EditText {

    var currentItemNameTextString: String? = null
    var currentItemNameTextForQuantity: String? = null

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun init(context: Context?, attrs: AttributeSet?) {

        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.DbAttachedEditText)
        val item_name_type = typedArray.getInteger(R.styleable.DbAttachedEditText_item_name_type, -1)

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (item_name_type == 1 && currentItemNameTextString != null && !currentItemNameTextString.equals("") && !s.toString().equals(currentItemNameTextString)) {
                    ItemsListDAO.changeproductItemName(currentItemNameTextString!!, s.toString())
                    currentItemNameTextString = s.toString()
                }
                else if (item_name_type == 2 && currentItemNameTextForQuantity != null && !currentItemNameTextForQuantity.equals("")) {
                    ItemsListDAO.changeproductItemQuantity(currentItemNameTextForQuantity!!, s.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        if(item_name_type == 1){
            currentItemNameTextString = text.toString()
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


}
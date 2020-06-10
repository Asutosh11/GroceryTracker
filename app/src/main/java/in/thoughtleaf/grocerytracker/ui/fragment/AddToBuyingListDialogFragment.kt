package `in`.thoughtleaf.grocerytracker.ui.fragment

import `in`.thoughtleaf.grocerytracker.customviews.SpeechToTextEditText
import `in`.thoughtleaf.grocerytracker.data.dao.BuyingListDAO
import `in`.thoughtleaf.grocerytracker.data.event.AddedToBuyingListEvent
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.thoughtleaf.grocerytracker.R
import org.greenrobot.eventbus.EventBus
import java.util.*

class AddToBuyingListDialogFragment : DialogFragment() {

    lateinit var addBtn : Button
    lateinit var itemNameET : SpeechToTextEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_add_to_buying_list, container, false)

        addBtn = root.findViewById(R.id.btn_add)
        itemNameET = root.findViewById(R.id.et_item_name)

        addBtn.setOnClickListener(View.OnClickListener {

           if(itemNameET.text != null && !itemNameET.text.toString().equals("")){
               if(BuyingListDAO.getAllOfflineDataNamesLowerCase().contains(itemNameET.text.toString().toLowerCase())){
                   Toast.makeText(context, "Product already exists in buying list", Toast.LENGTH_LONG).show()
               }
               else{
                   val product = Product(itemNameET.text.toString(), "", "", "")
                   BuyingListDAO.addProduct(product)
                   EventBus.getDefault().postSticky(AddedToBuyingListEvent(itemNameET.text.toString()))
                   dismiss()
               }

            }
            else{
                Toast.makeText(context, "Please enter a product", Toast.LENGTH_LONG).show()
            }

        })

        itemNameET.setTag(1)
        drawableRightClickListener(itemNameET)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        dialog.getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        return dialog
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){

            val result: ArrayList<String> = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!

            if(requestCode == 1){
                itemNameET.setText(result.get(0))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun drawableRightClickListener(editText: SpeechToTextEditText){
        editText.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= editText.getRight() - editText.getCompoundDrawables().get(
                        DRAWABLE_RIGHT
                    ).getBounds().width()
                ) {
                    startSpeechToTextDialog(editText.getTag() as Int)
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    fun startSpeechToTextDialog(requestCode: Int){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak")
        try {
            startActivityForResult(intent, requestCode)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                context?.applicationContext,
                "Sorry your device not supported",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
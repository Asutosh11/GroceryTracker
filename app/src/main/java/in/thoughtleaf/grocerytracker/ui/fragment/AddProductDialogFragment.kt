package `in`.thoughtleaf.grocerytracker.ui.fragment

import `in`.thoughtleaf.grocerytracker.customviews.SpeechToTextEditText
import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import `in`.thoughtleaf.grocerytracker.data.event.NewProductAddedEvent
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import `in`.thoughtleaf.grocerytracker.util.AppConstantsUtil
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.*
import android.view.View.GONE
import android.view.View.OnTouchListener
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.thoughtleaf.grocerytracker.R
import org.greenrobot.eventbus.EventBus
import java.util.*


class AddProductDialogFragment : DialogFragment() {

    lateinit var categorySpinner : Spinner
    lateinit var addBtn : Button
    lateinit var itemNameET : SpeechToTextEditText
    lateinit var newCategoryNameET : SpeechToTextEditText
    lateinit var labelExistingCategoriesTV : TextView
    lateinit var titleDialogTV : TextView

    lateinit var additionScreenType : AppConstantsUtil.Companion.AdditionScreenType


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_add_item, container, false)

        categorySpinner = root.findViewById(R.id.category_spinner)
        addBtn = root.findViewById(R.id.btn_add)
        itemNameET = root.findViewById(R.id.et_item_name)
        newCategoryNameET = root.findViewById(R.id.et_new_category_name)
        labelExistingCategoriesTV = root.findViewById(R.id.tv_label_existing_categories)
        titleDialogTV = root.findViewById(R.id.title_dialog_tv)

        // get data from parent fragment/activity
        if(arguments != null){
            if(arguments?.getString("dialog_type").equals("add_product")){
                titleDialogTV.text = "Add a new product"
                additionScreenType = AppConstantsUtil.Companion.AdditionScreenType.AddProduct()
                newCategoryNameET.visibility = GONE

                var listOfProducts : ArrayList<String> = ItemsListDAO.getAllOfflineItemsCategories()
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this!!.context!!,
                    R.layout.spinner_item_dark,
                    listOfProducts
                )
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                categorySpinner.setAdapter(adapter)
                categorySpinner.setSelection(0)

                val selectedCategory = arguments?.getString("selected_category")
                val allPresentCategors : ArrayList<String> = ItemsListDAO.getAllOfflineItemsCategories()
                for(i:Int in 0 until allPresentCategors.size){
                    if(listOfProducts.get(i).equals(selectedCategory)){
                        categorySpinner.setSelection(i)
                        break
                    }
                }
            }
            else{
                titleDialogTV.text = "Add a new category"
                additionScreenType = AppConstantsUtil.Companion.AdditionScreenType.AddCategory()
                itemNameET.visibility = GONE
                categorySpinner.visibility = GONE
                labelExistingCategoriesTV.visibility = GONE
            }
        }

        addBtn.setOnClickListener(View.OnClickListener {

            when (additionScreenType) {
                is AppConstantsUtil.Companion.AdditionScreenType.AddProduct -> {

                    if(itemNameET.text == null || itemNameET.text.toString().equals("")){
                        Toast.makeText(context, getString(R.string.enter_item), Toast.LENGTH_LONG).show()
                    }
                    else if(categorySpinner.isEnabled && categorySpinner.selectedItem != null && !categorySpinner.selectedItem.toString().equals("")){
                        if(!ItemsListDAO.getAllOfflineItemsNamesLowerCase().contains(itemNameET.text.toString().toLowerCase())){
                            ItemsListDAO.saveOneProduct(
                                Product(
                                    itemNameET.text.toString(),
                                    "1",
                                    "Pack",
                                    categorySpinner.selectedItem.toString()
                                )
                            )
                            Toast.makeText(context, getString(R.string.new_product_added) + " " + getString(R.string.to) + " " +
                                    categorySpinner.selectedItem.toString(), Toast.LENGTH_LONG).show()
                            closeDialog(additionScreenType)
                        }
                        else{
                            Toast.makeText(context, "Product already exists", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is AppConstantsUtil.Companion.AdditionScreenType.AddCategory -> {

                    if(newCategoryNameET.text != null && !newCategoryNameET.text.toString().equals("")){
                        val allPresentCategors : ArrayList<String> = ItemsListDAO.getAllOfflineItemsCategories()
                        for(i:Int in 0 until allPresentCategors.size){
                            allPresentCategors.add(allPresentCategors.get(i).toLowerCase())
                        }

                        if(!allPresentCategors.contains(newCategoryNameET.text.toString().toLowerCase())){
                            ItemsListDAO.saveCategoryWithNullProduct(newCategoryNameET.text.toString())
                            Toast.makeText(context, getString(R.string.new_category_added), Toast.LENGTH_LONG).show()
                            closeDialog(additionScreenType)
                        }
                        else{
                            Toast.makeText(context, getString(R.string.category_exists), Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Toast.makeText(context, getString(R.string.enter_new_category), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        itemNameET.setTag(1)
        newCategoryNameET.setTag(2)
        drawableRightClickListener(itemNameET)
        drawableRightClickListener(newCategoryNameET)

        return root
    }

    private fun closeDialog(additionScreenType: AppConstantsUtil.Companion.AdditionScreenType) {
        when (additionScreenType) {
            is AppConstantsUtil.Companion.AdditionScreenType.AddProduct -> {
                EventBus.getDefault().postSticky(
                    NewProductAddedEvent(
                        itemNameET.text.toString(),
                        categorySpinner.selectedItem.toString()
                    )
                )
            }
            is AppConstantsUtil.Companion.AdditionScreenType.AddCategory -> {
                if(categorySpinner.selectedItem != null){
                    EventBus.getDefault().postSticky(categorySpinner.selectedItem.toString())
                }
            }
        }

        dismiss()
    }

    private fun initRecyclerView(list : ArrayList<String>) {

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

        if(resultCode == RESULT_OK){

            val result: ArrayList<String> = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!

            if(requestCode == 1){
                itemNameET.setText(result.get(0))
            }
            else if(requestCode == 2){
                newCategoryNameET.setText(result.get(0))
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    fun drawableRightClickListener(editText: SpeechToTextEditText){
        editText.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= editText.getRight() - editText.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()) {
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
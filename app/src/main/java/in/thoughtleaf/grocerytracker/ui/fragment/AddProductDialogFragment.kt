package `in`.thoughtleaf.grocerytracker.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import `in`.thoughtleaf.grocerytracker.data.event.NewProductAddedEvent
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import `in`.thoughtleaf.grocerytracker.util.AppConstantsUtil
import com.thoughtleaf.grocerytracker.R
import org.greenrobot.eventbus.EventBus


class AddProductDialogFragment : DialogFragment() {

    lateinit var categorySpinner : Spinner
    lateinit var addBtn : Button
    lateinit var itemNameET : EditText
    lateinit var newCategoryNameET : EditText
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
                        ItemsListDAO.saveOneProduct(
                            Product(
                                itemNameET.text.toString(),
                                "1",
                                "Pack",
                                categorySpinner.selectedItem.toString()
                            )
                        )
                        Toast.makeText(context, getString(R.string.new_product_added), Toast.LENGTH_LONG).show()
                        closeDialog(additionScreenType)
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
                EventBus.getDefault().postSticky(categorySpinner.selectedItem.toString())

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
}
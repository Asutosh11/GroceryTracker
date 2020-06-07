package `in`.thoughtleaf.grocerytracker.ui.fragment

import `in`.thoughtleaf.grocerytracker.data.dao.BuyingListDAO
import `in`.thoughtleaf.grocerytracker.data.event.AddedToBuyingListEvent
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.thoughtleaf.grocerytracker.R
import org.greenrobot.eventbus.EventBus

class AddToBuyingListDialogFragment : DialogFragment() {

    lateinit var addBtn : Button
    lateinit var itemNameET : EditText

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
}
package `in`.thoughtleaf.grocerytracker.ui.fragment

import `in`.thoughtleaf.grocerytracker.customviews.SpeechToTextEditText
import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import `in`.thoughtleaf.grocerytracker.data.event.NewCategoryAddedEvent
import `in`.thoughtleaf.grocerytracker.data.event.NewProductAddedEvent
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import `in`.thoughtleaf.grocerytracker.ui.adapter.ProductRvAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thoughtleaf.grocerytracker.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * A placeholder fragment containing a simple view.
 */
class HomeTabOneFragment : Fragment(){

    lateinit var recyclerView : RecyclerView
    lateinit var searchACT : AutoCompleteTextView
    lateinit var btnCompleteList : Button
    lateinit var tvCategoryLabel : TextView
    lateinit var fab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initRecyclerView(list : ArrayList<Product>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.line_separator)!!)
        recyclerView.addItemDecoration(itemDecorator)
        val adapter = ProductRvAdapter(list, context)
        recyclerView.adapter = adapter

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_tab_one, container, false)

        recyclerView = root.findViewById(R.id.recycler_view)
        searchACT = root.findViewById(R.id.search_box_act)
        btnCompleteList = root.findViewById(R.id.categories_button)
        tvCategoryLabel = root.findViewById(R.id.tv_label_for_category)
        fab = root.findViewById(R.id.fab) as FloatingActionButton

        fab.setOnClickListener(View.OnClickListener {
            val fm = activity?.supportFragmentManager
            val addProductDialogFragment =
                AddProductDialogFragment()
            val args = Bundle()
            args.putString("dialog_type", "add_product")
            if(tvCategoryLabel.text != null && !tvCategoryLabel.text.toString().equals("")){
                var selectedCAtegory = tvCategoryLabel.text.toString()
                args.putString("selected_category", selectedCAtegory)
            }
            addProductDialogFragment.setArguments(args)
            addProductDialogFragment.show(fm!!, "dialog_fragment")
        })

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }

        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, ItemsListDAO.getAllOfflineItemsNames())
        searchACT.setThreshold(2)
        searchACT.setAdapter(adapter)

        searchACT.setOnItemClickListener(OnItemClickListener { parent, arg1, pos, id ->
            val selectedText = parent.getItemAtPosition(pos) as String
            if(selectedText != null && !selectedText.equals("")){
                tvCategoryLabel.visibility = GONE
                initRecyclerView(ItemsListDAO.searchItemByName(selectedText))
            }
        })

        btnCompleteList.setOnClickListener {
            val fm = activity?.supportFragmentManager
            val categoriesDialogFragment =
                CategoriesDialogFragment()
            categoriesDialogFragment.show(fm!!, "dialog_fragment")
        }

        // when the screen loads for the first time, show Essentials
        tvCategoryLabel.visibility = VISIBLE
        tvCategoryLabel.setText("Essentials")
        initRecyclerView(ItemsListDAO.searchItemNamesByCategory("Essentials"))

        searchACT.setTag(1)
        drawableRightClickListener(searchACT)

        searchACT.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s?.length!! > 0){
                    searchACT.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_black_24dp, 0);
                }
                else{
                    searchACT.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_voice_black_24dp, 0);
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        return root
    }


    override fun onDestroy() {

        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): HomeTabOneFragment {
            return HomeTabOneFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    /**
     * New category added listener
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NewCategoryAddedEvent?) {
        if(event != null && event.selectedCategoryName != null && !event.selectedCategoryName.equals("")){

            var listOfProducts = ItemsListDAO.searchItemNamesByCategory(event.selectedCategoryName)
            if(listOfProducts != null && listOfProducts.size > 0){
                initRecyclerView(listOfProducts)
                tvCategoryLabel.visibility = VISIBLE
                tvCategoryLabel.setText(event.selectedCategoryName)
                searchACT.setText("")
            }
        }
    }

    /**
     * New product added listener
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NewProductAddedEvent?) {
        if(event != null && event.newProductCategory != null && !event.newProductCategory.equals("")){

            var listOfProducts = ItemsListDAO.searchItemNamesByCategory(event.newProductCategory)
            if(listOfProducts != null && listOfProducts.size > 0 && event.newProductCategory.equals(tvCategoryLabel.text.toString())){
                initRecyclerView(listOfProducts)
                searchACT.setText("")
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){

            val result: ArrayList<String> = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!

            if(requestCode == 1){
                searchACT.setText(result.get(0))
                searchACT.showDropDown()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun drawableRightClickListener(autoCompleteTextView: AutoCompleteTextView){
        autoCompleteTextView.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()) {
                    if(autoCompleteTextView.text.toString() != null && !autoCompleteTextView.text.toString().equals("")){
                        autoCompleteTextView.setText("")
                    }
                    else if(autoCompleteTextView.text.toString() != null || autoCompleteTextView.text.toString().equals("")){
                        startSpeechToTextDialog(autoCompleteTextView.getTag() as Int)
                    }
                    return@OnTouchListener true
                }
            }
            false
        })
    }
}
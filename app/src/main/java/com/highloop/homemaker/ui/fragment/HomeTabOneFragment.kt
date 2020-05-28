package com.highloop.homemaker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.highloop.homemaker.R
import com.highloop.homemaker.data.dao.ItemsListDAO
import com.highloop.homemaker.data.event.CategorySelectEvent
import com.highloop.homemaker.data.event.NewProductAddedEvent
import com.highloop.homemaker.data.pojo.Product
import com.highloop.homemaker.ui.adapter.ProductRvAdapter
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
        btnCompleteList = root.findViewById(R.id.list_button)
        tvCategoryLabel = root.findViewById(R.id.tv_label_for_category)
        fab = root.findViewById(R.id.fab)

        fab.setOnClickListener(View.OnClickListener {
            val fm = activity?.supportFragmentManager
            val addProductDialogFragment = AddProductDialogFragment()
            val args = Bundle()
            args.putString("dialog_type", "add_product")
            if(tvCategoryLabel.text != null && !tvCategoryLabel.text.toString().equals("")){
                var selectedCAtegory = tvCategoryLabel.text.toString().replace("Category: ", "")
                args.putString("selected_category", selectedCAtegory)
            }
            addProductDialogFragment.setArguments(args)
            addProductDialogFragment.show(fm!!, "dialog_fragment")
        })

        return root
    }


    override fun onDestroy() {
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    override fun onResume() {
        super.onResume()

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
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
            val categoriesDialogFragment = CategoriesDialogFragment()
            categoriesDialogFragment.show(fm!!, "dialog_fragment")
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: CategorySelectEvent?) {
        if(event != null && event.selectedCategoryName != null && !event.selectedCategoryName.equals("")){

            var listOfProducts = ItemsListDAO.searchItemNamesByCategory(event.selectedCategoryName)
            if(listOfProducts != null && listOfProducts.size > 0){
                initRecyclerView(listOfProducts)
                tvCategoryLabel.visibility = VISIBLE
                tvCategoryLabel.setText("Category: "+event.selectedCategoryName)
                searchACT.setText("")
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NewProductAddedEvent?) {
        if(event != null && event.newProductName != null && !event.newProductName.equals("")){

            var listOfProducts = ItemsListDAO.searchItemByName(event.newProductName)
            if(listOfProducts != null && listOfProducts.size > 0){
                initRecyclerView(listOfProducts)
                searchACT.setText("")
            }
        }
    }

}
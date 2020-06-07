package `in`.thoughtleaf.grocerytracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.thoughtleaf.grocerytracker.data.dao.BuyingListDAO
import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import `in`.thoughtleaf.grocerytracker.data.event.AddedToBuyingListEvent
import `in`.thoughtleaf.grocerytracker.data.event.NewProductAddedEvent
import `in`.thoughtleaf.grocerytracker.data.pojo.Product
import `in`.thoughtleaf.grocerytracker.ui.adapter.BuyingListRvAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.thoughtleaf.grocerytracker.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class HomeTabTwoFragment : Fragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var addProductButton : Button
    var adapter : BuyingListRvAdapter? = null
    var listOfProductsToBuy : ArrayList<Product>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initRecyclerView(list : ArrayList<Product>) {
        listOfProductsToBuy = list
        recyclerView.layoutManager = LinearLayoutManager(context)
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.line_separator)!!)
        recyclerView.addItemDecoration(itemDecorator)
        adapter = BuyingListRvAdapter(listOfProductsToBuy, context)
        recyclerView.adapter = adapter

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_tab_two, container, false)
        recyclerView = root.findViewById(R.id.recycler_view)
        addProductButton = root.findViewById(R.id.add_product)

        addProductButton.setOnClickListener(View.OnClickListener {

            val fm = activity?.supportFragmentManager
            val addToBuyingListDialogFrag = AddToBuyingListDialogFragment()
            addToBuyingListDialogFrag.show(fm!!, "dialog_fragment")
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }

        initRecyclerView(BuyingListDAO.getAllOfflineData())
    }

    override fun onDestroy() {

        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

        super.onDestroy()
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
        fun newInstance(sectionNumber: Int): HomeTabTwoFragment {
            return HomeTabTwoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    /**
     * New product added to buying list listener
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AddedToBuyingListEvent?) {
        if(event != null && event.newProductName != null && !event.newProductName.equals("")){

            listOfProductsToBuy?.add(Product(event.newProductName, "", "", ""))
            if(listOfProductsToBuy != null && listOfProductsToBuy?.size!! > 0){
                // initRecyclerView(listOfProductsToBuy!!)
                adapter?.notifyDataSetChanged()
            }
        }
    }
}
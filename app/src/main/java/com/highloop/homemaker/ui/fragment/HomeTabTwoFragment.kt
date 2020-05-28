package com.highloop.homemaker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.highloop.homemaker.R
import com.highloop.homemaker.data.dao.BuyingListDAO
import com.highloop.homemaker.data.pojo.Product
import com.highloop.homemaker.ui.adapter.BuyingListRvAdapter
import com.highloop.homemaker.ui.adapter.ProductRvAdapter
import java.util.ArrayList

/**
 * A placeholder fragment containing a simple view.
 */
class HomeTabTwoFragment : Fragment() {

    lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initRecyclerView(list : ArrayList<Product>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = BuyingListRvAdapter(list, context)
        recyclerView.adapter = adapter

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home_tab_two, container, false)
        recyclerView = root.findViewById(R.id.recycler_view)

        return root
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView(BuyingListDAO.getAllOfflineData())
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
}
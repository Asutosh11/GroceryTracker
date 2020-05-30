package `in`.thoughtleaf.grocerytracker.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.highloop.homemaker.R
import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import `in`.thoughtleaf.grocerytracker.ui.adapter.CategoriesRvAdapter
import java.util.ArrayList


class CategoriesDialogFragment : DialogFragment() {

    lateinit var recyclerView : RecyclerView
    lateinit var titleTV : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_categories, container, false)

        recyclerView = root.findViewById(R.id.recycler_view)
        titleTV = root.findViewById(R.id.tv_title)

        initRecyclerView(ItemsListDAO.getAllOfflineItemsCategories())

        titleTV.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is TextView) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        val fm = activity?.supportFragmentManager
                        val addProductDialogFragment =
                            AddProductDialogFragment()
                        val args = Bundle()
                        args.putString("dialog_type", "add_category")
                        addProductDialogFragment.setArguments(args)
                        addProductDialogFragment.show(fm!!, "dialog_fragment")
                        this.dismiss()
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }

        return root
    }

    private fun initRecyclerView(list : ArrayList<String>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter =
            CategoriesRvAdapter(list, this)
        recyclerView.adapter = adapter
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
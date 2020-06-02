package `in`.thoughtleaf.grocerytracker.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import `in`.thoughtleaf.grocerytracker.ui.fragment.HomeTabOneFragment
import `in`.thoughtleaf.grocerytracker.ui.fragment.HomeTabTwoFragment
import com.thoughtleaf.grocerytracker.R

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        if(position == 0){
            return HomeTabOneFragment.newInstance(position + 1)
        }
        else{
            return HomeTabTwoFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}
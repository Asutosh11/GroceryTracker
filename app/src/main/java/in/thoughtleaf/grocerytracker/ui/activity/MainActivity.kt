package `in`.thoughtleaf.grocerytracker.ui.activity

import `in`.thoughtleaf.grocerytracker.data.dao.ItemsListDAO
import `in`.thoughtleaf.grocerytracker.ui.adapter.SectionsPagerAdapter
import `in`.thoughtleaf.grocerytracker.ui.fragment.CreditsDialogFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.thoughtleaf.grocerytracker.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        ItemsListDAO.saveAllOfflineData(applicationContext)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        return when (id) {
            R.id.menu_credits -> {
                val fm = supportFragmentManager
                val infoDialogFragment =
                    CreditsDialogFragment()
                infoDialogFragment.show(fm!!, "dialog_fragment")
                true
            }
            R.id.menu_share -> {
                ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle(applicationContext.getString(R.string.app_name))
                    .setText("https://play.google.com/store/apps/details?id=com.thoughtleaf.grocerytracker")
                    .startChooser();
                true
            }
            R.id.menu_rate -> {
                val marketUri: Uri = Uri.parse("market://details?id=$packageName")
                startActivity(Intent(Intent.ACTION_VIEW, marketUri))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
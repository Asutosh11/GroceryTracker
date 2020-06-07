package `in`.thoughtleaf.grocerytracker.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.thoughtleaf.grocerytracker.R


class CreditsDialogFragment : DialogFragment() {

    lateinit var webView : WebView
    val mimeType = "text/html"
    val encoding = "UTF-8"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_info, container, false)

        webView = root.findViewById(R.id.web_view)

        var html : String = "<div>Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a></div>\n" +
                "\n" +
                "<br>\n"
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");

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
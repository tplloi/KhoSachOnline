package loitp.com.service

import android.os.AsyncTask
import android.view.View
import com.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.frm_list_chap.*
import loitp.com.adapter.AdapterListChap
import loitp.com.model.OneChap
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.ArrayList

class AsyncTaskLoadBookListChap(
    private val mUrl: String,
    private val onPreExecute: ((Unit) -> Unit),
    private val onPostExecute: ((ArrayList<OneChap>) -> Unit),
) : AsyncTask<Void, Void, Void>() {
    private val listChap = ArrayList<OneChap>()
    private var oneChap: OneChap? = null

    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke(Unit)
    }

    override fun doInBackground(vararg params: Void): Void? {
        val document: Document
        try {
            document = Jsoup.connect(mUrl).get()
            val link = document.select("div#tab-chapper ul li a")
            for (el in link) {
                oneChap = OneChap()
                oneChap?.let {
                    it.link = el.attr("href")
                    it.title = el.text()
                    listChap.add(it)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        onPostExecute.invoke(listChap)
    }
}

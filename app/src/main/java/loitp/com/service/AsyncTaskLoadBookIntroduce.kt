package loitp.com.service

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

class AsyncTaskLoadBookIntroduce(
    private val mUrl: String,
    private val onPreExecute: ((Unit) -> Unit),
    private val onSuccess: ((
        listString: ArrayList<String>,
        titleBook: String,
        authorBook: String,
        typeBook: String,
        viewBook: String,
    ) -> Unit
    ),
    private val onFailed: ((Unit) -> Unit),
) : AsyncTask<Void, Void, Void>() {
    private var runComplete = true
    private var titleBook = ""
    private var authorBook = ""
    private var typeBook = ""
    private var viewBook = ""
    private val listString = ArrayList<String>()

    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke(Unit)
    }

    override fun doInBackground(vararg params: Void): Void? {
        val document: Document
        try {
            document = Jsoup.connect(mUrl).get()

            val des = document.select("div#tab-over p")
            for (el in des) {
                listString.add(el.text())
            }

            // titleBook
            val eTitleBook = document.select("h1[class=story-intro-title]")
            for (el in eTitleBook) {
                titleBook = el.text()
            }
            // authorBook
            val eAuthorBook = document.select("p.story-intro-author ")
            for (el in eAuthorBook) {
                authorBook = el.text()
            }
            // typeBook
            val eTypeBook = document.select("p[class=story-intro-category]")
            for (el in eTypeBook) {
                typeBook = el.text()
            }
            // viewBook
            val eViewBook = document.select("p[class=story-intro-chapper]")
            for (el in eViewBook) {
                viewBook = el.text()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            runComplete = false
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        if (runComplete) {
            onSuccess.invoke(listString, titleBook, authorBook, typeBook, viewBook)
        } else {
            onFailed.invoke(Unit)
        }
        super.onPostExecute(result)
    }
}

package loitp.com.service

import android.os.AsyncTask
import loitp.com.util.Const
import loitp.com.util.Help.getBooleanFromData
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.SimpleHtmlSerializer
import org.jsoup.Jsoup

class AsyncTaskRead(
    private val link: String,
    private val positionWebview: Int,
    private val onPreExecute: ((Unit) -> Unit),
    private val onPostExecute: ((strHTML: String?, positionWebview: Int) -> Unit),
) : AsyncTask<Void, Void, Void>() {

    // 0 doInBkg ok
    // 1 err
    private var state = 0
    private var strHTML: String? = null

    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke(Unit)
    }

    override fun doInBackground(vararg params: Void): Void? {
        val newPage: String
        try {
            val swReadNightModeState = getBooleanFromData(Const.SETTING_READ_NIGHT_MODE)
            val theme: String = if (swReadNightModeState) {
                "color:black; background-color: white;"
            } else {
                "color:white; background-color: black;"
            }
            val document = Jsoup.connect(link).get()

            // Elements newsRawTag = doc.select("div#content-home");
            val newsRawTag = document.select("div[class=story-detail-content]")
            newPage = newsRawTag.html()
            val htmlCleaner = HtmlCleaner()
            val cleanerProp = htmlCleaner.properties
            val tagNode = HtmlCleaner(cleanerProp).clean(newPage)
            val htmlSerializer = SimpleHtmlSerializer(cleanerProp)
            val strPish = ("<html><head><style type=\"text/css\">body {text-align: justify;" + theme
                    + "}</style></head><body>")
            val strPas = "<br/><br/><br/><br/></html>"
            strHTML = (strPish
                    + "<br/><br/></br></br> Cám ơn vì đã sử dụng ứng dụng của mình. Chúc các bạn luôn đọc sách vui vẻ:)"
                    + htmlSerializer.getAsString(tagNode) + strPas)

        } catch (e: Exception) {
            e.printStackTrace()
            state = 1
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        onPostExecute.invoke(strHTML, positionWebview)
    }
}

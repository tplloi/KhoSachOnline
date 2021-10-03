package loitp.com.service

import android.os.AsyncTask
import android.util.Log
import com.core.base.BaseApplication
import com.core.utilities.LLog
import com.core.utilities.LStoreUtil
import loitp.com.model.Book
import loitp.com.util.ConvertUtil
import loitp.com.util.Help

class AsyncTaskParseJSON(
    private val mFileName: String,
    private val onPreExecute: ((Unit) -> Unit),
    private val onSuccess: ((MutableList<Book>) -> Unit),
    private val onFailed: ((Unit) -> Unit)
) :
    AsyncTask<Void, Void, Void>() {
    private val logTag = "loitpp" + javaClass.simpleName

    private fun log(msg: String) {
        LLog.d(logTag, msg)
    }

    private var runComplete = true
    private val listBook: MutableList<Book> = ArrayList()

    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke(Unit)
    }

    override fun doInBackground(vararg params: Void): Void? {
        log("doInBackground mFileName $mFileName")
        val strData = LStoreUtil.readTxtFromFolder(folderName = null, fileName = mFileName)
        log("doInBackground strData $strData")
        val listBookTmp: ArrayList<Book> =
            ConvertUtil.convert(strData)
        if (listBookTmp.isNullOrEmpty()) {
            runComplete = false
        } else {
            listBookTmp.sortedBy {
                it.tit
            }
            listBook.addAll(listBookTmp)
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        if (!runComplete) {
            onFailed.invoke(Unit)
        }
        onSuccess.invoke(listBook)
        super.onPostExecute(result)
    }
}

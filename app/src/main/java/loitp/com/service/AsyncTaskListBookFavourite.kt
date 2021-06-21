package loitp.com.service

import android.os.AsyncTask
import loitp.com.db.SQLiteHelper
import loitp.com.model.BookFavourite
import java.util.*

class AsyncTaskListBookFavourite(
    private val onPreExecute: ((Unit) -> Unit),
    private val onPostExecute: ((MutableList<BookFavourite>) -> Unit),
) : AsyncTask<Void, Void, Void>() {
    private val listBookFavourite: MutableList<BookFavourite> = ArrayList()

    override fun onPreExecute() {
        onPreExecute.invoke(Unit)
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void): Void? {
        val sqLiteHelper = SQLiteHelper()
        val alTmp: List<BookFavourite> = sqLiteHelper.allBooks.sortedBy {
            it.tit
        }
        listBookFavourite.addAll(alTmp)
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        onPostExecute.invoke(listBookFavourite)
    }
}

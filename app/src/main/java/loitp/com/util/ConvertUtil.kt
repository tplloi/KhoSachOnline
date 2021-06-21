package loitp.com.util

import com.core.base.BaseApplication
import com.google.gson.reflect.TypeToken
import loitp.com.model.Book
import loitp.com.model.WrapperBooks

object ConvertUtil {

    fun convert(json: String?): ArrayList<Book> {
        val listBook = ArrayList<Book>()
        if (json.isNullOrEmpty()) {
            return listBook
        }
        val type = object : TypeToken<WrapperBooks>() {
        }.type
        val wrapperBooks: WrapperBooks = BaseApplication.gson.fromJson(json, type)
        wrapperBooks.books?.book?.let {
            listBook.addAll(it)
        }
        return listBook
    }
}

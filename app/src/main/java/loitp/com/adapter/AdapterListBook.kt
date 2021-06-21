package loitp.com.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.core.utilities.LImageUtil
import com.core.utilities.LUIUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.views.setSafeOnClickListener
import loitp.com.R
import loitp.com.model.Book
import java.util.*

class AdapterListBook(
    private val mContext: Context,
    private var listBook: List<Book>,
    private val onClickRoot: ((book: Book) -> Unit),
    private val onClickAddFavourite: ((book: Book) -> Unit),
) :
    BaseAdapter(), Filterable {

    private var valueFilter: ValueFilter? = null
    private val listBookFilter: List<Book> = listBook

    override fun getCount(): Int {
        return listBook.size
    }

    override fun getItem(pos: Int): Any {
        return listBook[pos]
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View? {
        var view = convertView
        val oneView: ViewHolder
        val layoutInflater = (mContext as Activity).layoutInflater
        if (view == null) {
            oneView = ViewHolder()
            view = layoutInflater.inflate(R.layout.row_item_book, null)
            oneView.iv = view.findViewById(R.id.iv)
            oneView.layoutRootView = view.findViewById(R.id.layoutRootView)
            oneView.tvTitle = view.findViewById(R.id.tvTitle)
            oneView.tvAuthor = view.findViewById(R.id.tvAuthor)
            oneView.btAddFavourite = view.findViewById(R.id.btAddFavourite)
            view.tag = oneView
        } else {
            oneView = view.tag as ViewHolder
        }
        val book = listBook[position]
        LUIUtil.setMarquee(tv = oneView.tvTitle, text = book.tit)
        LUIUtil.setMarquee(tv = oneView.tvAuthor, text = "Tác giả: " + book.aut)
        LImageUtil.load(
            context = mContext,
            any = listBook[position].thumb,
            imageView = oneView.iv,
            resPlaceHolder = R.drawable.bkg_place_holder,
            resError = R.drawable.bkg_place_holder,
            transformation = null,
            drawableRequestListener = null
        )
        oneView.layoutRootView?.setSafeOnClickListener {
            onClickRoot.invoke(book)
        }
        oneView.btAddFavourite?.setSafeOnClickListener {
            onClickAddFavourite.invoke(book)
        }
        return view
    }

    override fun getFilter(): Filter? {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter
    }

    class ViewHolder {
        var layoutRootView: CardView? = null
        var tvRound: TextView? = null
        var tvTitle: TextView? = null
        var tvAuthor: TextView? = null
        var iv: ImageView? = null
        var btAddFavourite: FloatingActionButton? = null
    }

    private inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint != null && constraint.isNotEmpty()) {
                val filterList = ArrayList<Book>()
                for (i in listBookFilter.indices) {
                    if (listBookFilter[i].tit!!.toUpperCase(Locale.getDefault())
                            .contains(constraint.toString().toUpperCase(Locale.getDefault()))
                    ) {
                        val b = Book()
                        b.aut = listBookFilter[i].aut
                        b.url = listBookFilter[i].url
                        b.thumb = listBookFilter[i].thumb
                        b.tit = listBookFilter[i].tit
                        b.rq = listBookFilter[i].rq
                        filterList.add(b)
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = listBookFilter.size
                results.values = listBookFilter
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            listBook = results.values as ArrayList<Book>
            notifyDataSetChanged()
        }
    }

}

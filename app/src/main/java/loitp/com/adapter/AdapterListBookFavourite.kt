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
import loitp.com.model.BookFavourite
import java.util.*

class AdapterListBookFavourite(
    private val context: Context,
    private var listBookFavourite: MutableList<BookFavourite>,
    private val onClickRoot: ((book: BookFavourite) -> Unit),
    private val onClickRemove: ((book: BookFavourite) -> Unit),
) : BaseAdapter(), Filterable {

    private var valueFilter: ValueFilter? = null
    private val listBookFavouriteFilter: List<BookFavourite>

    init {
        listBookFavouriteFilter = listBookFavourite
    }

    override fun getCount(): Int {
        return listBookFavourite.size
    }

    override fun getItem(pos: Int): Any {
        return listBookFavourite[pos]
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, arg2: ViewGroup): View {
        var view = convertView
        val oneView: ViewHolder
        val layoutInflater = (context as Activity).layoutInflater
        if (view == null) {
            oneView = ViewHolder()
            view = layoutInflater.inflate(R.layout.row_item_book, null)
            oneView.iv = view.findViewById(R.id.iv)
            oneView.xmlMain = view.findViewById(R.id.layoutRootView)
            oneView.tvTitle = view.findViewById(R.id.tvTitle)
            oneView.tvAuthor = view.findViewById(R.id.tvAuthor)
            oneView.progressBar = view.findViewById(R.id.progressBar)
            oneView.btAddFavourite = view.findViewById(R.id.btAddFavourite)
            view.tag = oneView
        } else oneView = view.tag as ViewHolder

        val book = listBookFavourite[position]
        LUIUtil.setMarquee(tv = oneView.tvTitle, text = book.tit)
        LUIUtil.setMarquee(
            tv = oneView.tvAuthor,
            text = "Tác giả: " + book.aut
        )
        oneView.btAddFavourite?.setImageResource(R.drawable.ic_close_black_24dp)
        LImageUtil.load(
            context = context,
            any = book.thumb,
            imageView = oneView.iv,
            resPlaceHolder = R.drawable.bkg_place_holder,
            resError = R.drawable.bkg_place_holder,
            transformation = null,
            drawableRequestListener = null
        )
        oneView.xmlMain?.setSafeOnClickListener {
            onClickRoot.invoke(book)
        }
        oneView.btAddFavourite?.setSafeOnClickListener {
            onClickRemove.invoke(book)
        }
        return view!!
    }

    override fun getFilter(): Filter? {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter
    }

    class ViewHolder {
        var xmlMain: CardView? = null
        var tvRound: TextView? = null
        var tvTitle: TextView? = null
        var tvAuthor: TextView? = null
        var iv: ImageView? = null
        var progressBar: ProgressBar? = null
        var btAddFavourite: FloatingActionButton? = null
    }

    private inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint != null && constraint.isNotEmpty()) {
                val filterList = ArrayList<BookFavourite>()
                for (i in listBookFavouriteFilter.indices) {
                    if (listBookFavouriteFilter[i].tit!!.toUpperCase(Locale.getDefault())
                            .contains(constraint.toString().toUpperCase(Locale.getDefault()))
                    ) {
                        val bookFavourite = BookFavourite()
                        bookFavourite.aut = listBookFavouriteFilter[i].aut
                        bookFavourite.url = listBookFavouriteFilter[i].url
                        bookFavourite.thumb = listBookFavouriteFilter[i].thumb
                        bookFavourite.tit = listBookFavouriteFilter[i].tit
                        bookFavourite.rq = listBookFavouriteFilter[i].rq
                        filterList.add(bookFavourite)
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = listBookFavouriteFilter.size
                results.values = listBookFavouriteFilter
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            listBookFavourite = results.values as ArrayList<BookFavourite>
            notifyDataSetChanged()
        }
    }

}

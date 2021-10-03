package loitp.com.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_1_favourite.*
import loitp.com.R
import loitp.com.adapter.AdapterListBookFavourite
import loitp.com.db.SQLiteHelper
import loitp.com.model.BookFavourite
import loitp.com.service.AsyncTaskListBookFavourite
import loitp.com.ui.activity.ListChapActivity
import loitp.com.util.Const
import loitp.com.util.Help
import java.util.*

@LogTag("Frm1Favourite")
class FrmListBookFavourite : BaseFragment() {
    private var adapterListBookFavourite: AdapterListBookFavourite? = null
    private val listBookFavourite: MutableList<BookFavourite> = ArrayList()
    private var asyncTaskListBookFavourite: AsyncTaskListBookFavourite? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_1_favourite
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btRefresh.setSafeOnClickListener {
            btRefresh.isEnabled = false
            listBookFavourite.clear()
            adapterListBookFavourite?.notifyDataSetChanged()
            loadDb()
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapterListBookFavourite?.filter?.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        adapterListBookFavourite = AdapterListBookFavourite(
            context = requireContext(),
            listBookFavourite = listBookFavourite,
            onClickRoot = {
                onClickRoot(bookFavourite = it)
            },
            onClickRemove = {
                onClickRemove(bookFavourite = it)
            }
        )
        gridView.adapter = adapterListBookFavourite
    }

    private fun loadDb() {
        asyncTaskListBookFavourite?.cancel(true)
        asyncTaskListBookFavourite = AsyncTaskListBookFavourite(
            onPreExecute = {
                progressBar?.visibility = View.VISIBLE
            },
            onPostExecute = {
                listBookFavourite.addAll(it)
                progressBar?.visibility = View.GONE
                adapterListBookFavourite?.notifyDataSetChanged()
                if (listBookFavourite.isNullOrEmpty()) {
                    tvMsg?.visibility = View.VISIBLE
                } else {
                    tvMsg?.visibility = View.GONE
                }
                btRefresh?.isEnabled = true
            }
        )
        asyncTaskListBookFavourite?.execute()
    }

    override fun onDestroy() {
        asyncTaskListBookFavourite?.cancel(true)
        super.onDestroy()
    }

    override fun onResume() {
        listBookFavourite.clear()
        adapterListBookFavourite?.notifyDataSetChanged()
        loadDb()
        super.onResume()
    }

    private fun onClickRoot(bookFavourite: BookFavourite) {
        Help.saveBooleanToData(Const.READ_FROM_FAVOURITE, true)
        val intent = Intent(context, ListChapActivity::class.java)
        intent.putExtra("mUrl", bookFavourite.url)
        intent.putExtra("mTitle", bookFavourite.tit)
        intent.putExtra("imgThumbnail", bookFavourite.thumb)
        intent.putExtra("positionChap", bookFavourite.pos)
        intent.putExtra("positionWebview", bookFavourite.poswv)
        Help.saveIntToData(Const.FAVOURITE_BOOK, bookFavourite.id)
        startActivity(intent)
        LActivityUtil.tranIn(context)
    }

    private fun onClickRemove(bookFavourite: BookFavourite) {
        fun removeBookFromListFavourite() {
            val sqLiteHelper = SQLiteHelper()
            if (sqLiteHelper.deleteBook(bookFavourite)) {
                showShortInformation(getString(R.string.delete_book_ok) + "\n" + bookFavourite.tit)
                listBookFavourite.remove(bookFavourite)
                adapterListBookFavourite?.notifyDataSetChanged()
                if (listBookFavourite.isNullOrEmpty()) {
                    tvMsg.visibility = View.VISIBLE
                } else {
                    tvMsg.visibility = View.GONE
                }
            } else {
                showShortInformation(getString(R.string.error))
            }
        }

        showBottomSheetOptionFragment(
            title = getString(R.string.warning_vn),
            message = getString(R.string.ask_remove_book),
            textButton1 = getString(R.string.yes),
            textButton2 = getString(R.string.no),
            onClickButton1 = {
                removeBookFromListFavourite()
            },
            onClickButton2 = {
                //do nothing
            }
        )
    }
}

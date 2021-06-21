package loitp.com.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.views.setSafeOnClickListener
import loitp.com.R
import loitp.com.model.OneChap
import java.util.*

class AdapterListChap(
    private val mContext: Context,
    private val listChap: ArrayList<OneChap>,
    private val positionChap: Int,
    private val positionWebview: Int,
    private val onClickRoot: ((position: Int, positionChap: Int, positionWebview: Int) -> Unit),
) : BaseAdapter() {

    override fun getCount(): Int {
        return listChap.size
    }

    override fun getItem(pos: Int): Any {
        return listChap[pos]
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(arg0: Int, arg1: View?, arg2: ViewGroup): View {
        var view = arg1
        val oneView: ViewHolder
        val layoutInflater = (mContext as Activity).layoutInflater
        if (view == null) {
            oneView = ViewHolder()
            view = layoutInflater.inflate(R.layout.view_one_chap, null)
            oneView.xmlMain = view.findViewById(R.id.layoutRootView)
            oneView.tvRound = view.findViewById(R.id.tvRound)
            oneView.tvTitle = view.findViewById(R.id.tvTitle)
            view.tag = oneView
        } else {
            oneView = view.tag as ViewHolder
        }
        val firstCharacter: String = try {
            val arrStr = listChap[arg0].title?.split(" ")?.toTypedArray()
            if (arrStr.isNullOrEmpty()) {
                "?"
            } else {
                arrStr[arrStr.size - 1]
            }
        } catch (e: Exception) {
            "?"
        }
        oneView.tvRound?.text = firstCharacter
        oneView.tvTitle?.text = listChap[arg0].title
        oneView.xmlMain?.setSafeOnClickListener {
            onClickRoot.invoke(arg0, positionChap, positionWebview)
        }
        return view!!
    }

    class ViewHolder {
        var xmlMain: LinearLayout? = null
        var tvRound: TextView? = null
        var tvTitle: TextView? = null
    }
}

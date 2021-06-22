package loitp.com.ui.fragment.listchap

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.core.utilities.LUIUtil
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_list_chap.*
import loitp.com.R
import loitp.com.adapter.AdapterListChap
import loitp.com.service.AsyncTaskLoadBookListChap
import loitp.com.ui.activity.ActRead

@LogTag("FrmListChap")
class FrmListChap : BaseFragment() {
    companion object {
        const val KEY_URL = "mUrl"
        const val KEY_POSITION_CHAP = "positionChap"
        const val KEY_POSITION_WEBVIEW = "positionWebview"
    }

    private var mUrl: String? = null
    private var positionChap = 0
    private var positionWebview = 0
    private var asyncTaskLoadBookListChap: AsyncTaskLoadBookListChap? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_list_chap
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        asyncTaskLoadBookListChap?.cancel(true)
        super.onDestroyView()
    }

    private fun setupViews() {
        arguments?.let {
            mUrl = it.getString(KEY_URL)
            positionChap = it.getInt(KEY_POSITION_CHAP)
            positionWebview = it.getInt(KEY_POSITION_WEBVIEW)
        }
        tvMsg?.setSafeOnClickListener {
            loadData()
        }
        loadData()
    }

    private fun loadData() {
        mUrl?.let { url ->
            asyncTaskLoadBookListChap?.cancel(true)
            asyncTaskLoadBookListChap = AsyncTaskLoadBookListChap(
                mUrl = url,
                onPreExecute = {
                    avLoadingIndicatorView?.visibility = View.VISIBLE
                    tvMsg?.visibility = View.GONE
                }
            ) { listChap ->
                avLoadingIndicatorView?.visibility = View.GONE
                if (listChap.isNullOrEmpty()) {
                    tvMsg?.visibility = View.VISIBLE
                } else {
                    val adapterListChap =
                        AdapterListChap(
                            mContext = requireContext(),
                            listChap = listChap,
                            positionChap = positionChap,
                            positionWebview = positionWebview
                        ) { position, positionChap, positionWebview ->
                            val i = Intent(context, ActRead::class.java)
                            i.putExtra(ActRead.KEY_POSITION, position)
                            if (position == positionChap) {
                                i.putExtra(ActRead.KEY_POSITION_WEBVIEW, positionWebview)
                            } else {
                                i.putExtra(ActRead.KEY_POSITION_WEBVIEW, 0)
                            }
                            val bundle = Bundle()
                            bundle.putSerializable(ActRead.KEY_OBJECT, listChap)
                            i.putExtra(ActRead.KEY_EXTRA, bundle)
                            startActivity(i)
                            LActivityUtil.tranIn(context)
                        }
                    gridView?.adapter = adapterListChap
                    gridView?.setSelection(positionChap)
                }
                if (positionChap < listChap.size) {
                    LUIUtil.setMarquee(
                        tv = tvMsgReadReminder,
                        text = "Bạn đang đọc tới chương " + listChap[positionChap].title
                    )
                }
            }
            asyncTaskLoadBookListChap?.execute()
        }
    }
}

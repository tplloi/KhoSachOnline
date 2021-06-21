package loitp.com.ui.fragment.listchap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LImageUtil
import kotlinx.android.synthetic.main.frm_list_chap_introduce.*
import loitp.com.R
import loitp.com.service.AsyncTaskLoadBookIntroduce
import java.util.*

@LogTag("FrmIntroduceBook")
class FrmIntroduceBook : BaseFragment() {
    companion object {
        const val KEY_URL = "mUrl"
        const val KEY_IMG_THUMBNAIL = "imgThumbnail"
    }

    private var mUrl: String? = null
    private var imgThumbnail: String? = ""
    private var asyncTaskLoadBookIntroduce: AsyncTaskLoadBookIntroduce? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_list_chap_introduce
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mUrl = it.getString(KEY_URL)
            imgThumbnail = it.getString(KEY_IMG_THUMBNAIL)
        }
        loadData()
    }

    override fun onDestroyView() {
        asyncTaskLoadBookIntroduce?.cancel(true)
        super.onDestroyView()
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        mUrl?.let { url ->
            asyncTaskLoadBookIntroduce?.cancel(true)
            asyncTaskLoadBookIntroduce = AsyncTaskLoadBookIntroduce(
                mUrl = url,
                onPreExecute = {
                    avLoadingIndicatorView?.visibility = View.VISIBLE
                    tvMsg?.visibility = View.GONE
                },
                onSuccess = { listString: ArrayList<String>,
                              titleBook: String,
                              authorBook: String,
                              typeBook: String,
                              viewBook: String
                    ->
                    val textDescription: String = try {
                        listString[0]
                    } catch (e: Exception) {
                        "No Description"
                    }
                    LImageUtil.load(
                        context = context,
                        any = imgThumbnail,
                        imageView = iv,
                        resPlaceHolder = R.drawable.bkg_place_holder,
                        resError = R.drawable.bkg_place_holder,
                        transformation = null,
                        drawableRequestListener = null
                    )

                    tvDescription?.text = textDescription.replace(". ", ".\n")
                    tvTitle?.text = titleBook
                    tvIntroduce?.text = "$authorBook\n$typeBook\n$viewBook"
                    avLoadingIndicatorView?.visibility = View.GONE
                },
                onFailed = {
                    tvMsg?.visibility = View.VISIBLE
                    tvMsg?.setOnClickListener {
                        loadData()
                    }
                    avLoadingIndicatorView?.visibility = View.GONE
                }
            )
            asyncTaskLoadBookIntroduce?.execute()
        }
    }
}

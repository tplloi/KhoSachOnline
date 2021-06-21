package loitp.com.ui.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LSocialUtil
import com.core.utilities.LUIUtil
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_0_introduce.*
import loitp.com.R
import loitp.com.util.Help

@LogTag("Frm0Introduce")
class FrmIntroduce : BaseFragment() {

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_0_introduce
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setup()
    }

    private fun setup() {
        tvInfo.text = Help.readTxtFromRawFolder(R.raw.aye_info)
        LUIUtil.setPullLikeIOSVertical(scrollView)
        btFbFanpage.setSafeOnClickListener {
            LSocialUtil.likeFacebookFanpage(activity)
        }
        btSendMailRequestBook.setSafeOnClickListener {
            Help.sendEmailRequestBook(context)
        }
        btSendMailSupport.setSafeOnClickListener {
            Help.sendEmail(context)
        }
    }
}
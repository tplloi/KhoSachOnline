package loitp.com.ui.fragment

import android.os.Bundle
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_0_quote.*
import loitp.com.R
import loitp.com.util.Help

@LogTag("Frm0Quote")
class FrmQuote : BaseFragment() {

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_0_quote
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupText()
        btCopy.setSafeOnClickListener {
            Help.setClipboard(text = tv.text.toString())
        }
        btNext.setSafeOnClickListener {
            setupText()
        }
    }

    private fun setupText() {
        val x = Help.getRandomQuote()
        tv.text = x
    }
}
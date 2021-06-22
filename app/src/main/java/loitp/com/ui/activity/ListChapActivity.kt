package loitp.com.ui.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_list_chap.*
import loitp.com.R
import loitp.com.ui.fragment.listchap.FrmIntroduceBook
import loitp.com.ui.fragment.listchap.FrmListChap

@LogTag("ListChapActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(true)
class ListChapActivity : BaseFontActivity() {
    private var mUrl: String? = null
    private var imgThumbnail: String? = null
    private var positionChap = 0
    private var positionWebview = 0

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_list_chap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        intent?.let {
            mUrl = it.getStringExtra("mUrl")
            val mTitle = it.getStringExtra("mTitle")
            imgThumbnail = it.getStringExtra("imgThumbnail")
            positionChap = it.getIntExtra("positionChap", 0)
            positionWebview = it.getIntExtra("positionWebview", 0)
            LUIUtil.setMarquee(tv = tvTitle, text = mTitle)
        }
    }

    private fun setupViews() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val adapterViewPager = AdapterViewPager(supportFragmentManager)
        viewPager.adapter = adapterViewPager
    }

    private inner class AdapterViewPager(fm: FragmentManager) : FragmentPagerAdapter(
        fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

        private var arrTitleViewpager: Array<String> = resources.getStringArray(R.array.title)

        override fun getItem(position: Int): Fragment {

            fun goToFrmIntroduce(): Fragment {
                val frm = FrmIntroduceBook()
                val bundle = Bundle()
                bundle.putString(FrmIntroduceBook.KEY_URL, mUrl)
                bundle.putString(FrmIntroduceBook.KEY_IMG_THUMBNAIL, imgThumbnail)
                frm.arguments = bundle
                return frm
            }

            fun goToFrmListChap(): Fragment {
                val frm = FrmListChap()
                val bundle = Bundle()
                bundle.putString(FrmListChap.KEY_URL, mUrl)
                bundle.putInt(FrmListChap.KEY_POSITION_CHAP, positionChap)
                bundle.putInt(FrmListChap.KEY_POSITION_WEBVIEW, positionWebview)
                frm.arguments = bundle
                return frm
            }

            return when (position) {
                0 -> {
                    goToFrmIntroduce()
                }
                1 -> {
                    goToFrmListChap()
                }
                else -> {
                    goToFrmIntroduce()
                }
            }
        }

        override fun getCount(): Int {
            return arrTitleViewpager.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return arrTitleViewpager[position]
        }
    }
}
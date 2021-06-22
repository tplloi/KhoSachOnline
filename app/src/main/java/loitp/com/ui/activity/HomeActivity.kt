package loitp.com.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.helper.gallery.GalleryCoreSplashActivity
import com.core.utilities.LActivityUtil
import com.core.utilities.LImageUtil
import com.core.utilities.LSocialUtil
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import loitp.com.R
import loitp.com.ui.fragment.FrmIntroduce
import loitp.com.ui.fragment.FrmQuote
import loitp.com.ui.fragment.FrmListBookFavourite
import loitp.com.ui.fragment.FrmListBook
import loitp.com.util.Const
import loitp.com.util.Help

@LogTag("MainActivity")
@IsFullScreen(false)
class HomeActivity : BaseFontActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var doubleBackToExitPressedOnce = false
    private var fragment: Fragment? = null
    private var layoutRootView: LinearLayout? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        //get view from NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val hView = navigationView.getHeaderView(0)
        layoutRootView = hView.findViewById(R.id.layoutRootView)
        val ivCover = layoutRootView?.findViewById<ImageView>(R.id.ivCover)
        ivCover?.let {
            LImageUtil.load(
                context = this,
                any = Const.APP_REVIEW_IMAGE,
                imageView = it
            )
        }

        fragment = FrmQuote()
        replaceFragment()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            doubleBackToExitPressedOnce = true
            showShortInformation(
                msg = getString(R.string.double_press_to_exit),
                isTopAnchor = false
            )
            Handler(Looper.getMainLooper()).postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionRate -> {
                LSocialUtil.rateApp(this)
            }
            R.id.actionMore -> {
                LSocialUtil.moreApp(this)
            }
            R.id.actionLikeFbFanpage -> {
                LSocialUtil.likeFacebookFanpage(this)
            }
            R.id.actionInviteApp -> {
                LSocialUtil.shareApp(this)
            }
            R.id.actionReportBug -> {
                Help.sendEmail(this)
            }
            R.id.actionSupport -> {
                LSocialUtil.chatMessenger(this)
            }
            R.id.actionPolicy -> {
                LSocialUtil.openBrowserPolicy(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        toolbar.title = item.title
        when (id) {
            R.id.nav0Gallery -> {
                val intent = Intent(this, GalleryCoreSplashActivity::class.java)
                intent.putExtra(Constants.AD_UNIT_ID_BANNER, getString(R.string.str_b))
                intent.putExtra(Constants.BKG_SPLASH_SCREEN, Constants.URL_IMG_5)
                //neu muon remove albumn nao thi cu pass id cua albumn do
                val removeAlbumFlickrList = ArrayList<String>()
                removeAlbumFlickrList.add(Constants.FLICKR_ID_STICKER)
                intent.putStringArrayListExtra(
                    Constants.KEY_REMOVE_ALBUM_FLICKR_LIST,
                    removeAlbumFlickrList
                )
                startActivity(intent)
                LActivityUtil.tranIn(this)
            }
            R.id.nav0Quote -> fragment = FrmQuote()
            R.id.nav0Introduce -> fragment = FrmIntroduce()
            R.id.nav1ListFavourite -> fragment = FrmListBookFavourite()
            R.id.nav1NgonTinh -> setBundleListBook(
                "0B0-bfr9v36LUN1VkM2JVV0k4WDQ",
                "nav_1_ngontinh"
            )
            R.id.nav1TruyenTeen -> setBundleListBook(
                "0B0-bfr9v36LUYlhEcy1RNmdfaEU",
                "nav_1_truyenteen"
            )
            R.id.nav1XuyenKhong -> setBundleListBook(
                "0B0-bfr9v36LUOUFEV2xfTHdqaWc",
                "nav_1_xuyenkhong"
            )
            R.id.nav1DamMy -> setBundleListBook("0B0-bfr9v36LUXzA4TUg4VHdVTXc", "nav_1_dammy")
            R.id.nav1TruyenVoz -> setBundleListBook(
                "0B0-bfr9v36LUTnJJUm96WmxjWXM",
                "nav_1_truyenvoz"
            )
            R.id.nav1KiemHiep -> setBundleListBook(
                "0B0-bfr9v36LUVHVqNzNIN2lfRlU",
                "nav_1_kiemhiep"
            )
            R.id.nav1SacHiep -> setBundleListBook("0B0-bfr9v36LUUTl4MHhJakhLM2s", "nav_1_sachiep")
            R.id.nav1TruyenNgan -> setBundleListBook(
                "0B0-bfr9v36LUdzJEeHduanpFSlE",
                "nav_1_truyenngan"
            )
            R.id.nav1DoThi -> setBundleListBook("0B0-bfr9v36LUTzJRTDFna2s3UlU", "nav_1_dothi")
            R.id.nav1TrinhTham -> setBundleListBook(
                "0B0-bfr9v36LUSnlqRWRKRDcyUUE",
                "nav_1_trinhtham"
            )
            R.id.nav1ChuyenThamKin -> setBundleListBook(
                "0B0-bfr9v36LUWXJTdE83eHNMWmM",
                "nav_1_chuyenthamkin"
            )
            R.id.nav1KinhDi -> setBundleListBook("0B0-bfr9v36LUZnNQQVh6aktJaHc", "nav_1_kinhdi")
            R.id.nav1TheLoaiKhac -> setBundleListBook(
                "0B0-bfr9v36LUaGJjYkc0d1l0X3c",
                "nav_1_theloaikhac"
            )
            R.id.nav2NtHienDai -> setBundleListBook(
                "0B0-bfr9v36LUVnN1REtQZjJOdG8",
                "nav_2_nt_hiendai"
            )
            R.id.nav2NtCoDai -> setBundleListBook(
                "0B0-bfr9v36LUaC1lVDdraUVuMVU",
                "nav_2_nt_codai"
            )
            R.id.nav2NtHe -> setBundleListBook("0B0-bfr9v36LUV1VxaVVGbzJfUVU", "nav_2_nt_he")
            R.id.nav2NtXuatBan -> setBundleListBook(
                "0B0-bfr9v36LUREFpbExicnlaMTA",
                "nav_2_nt_xuatban"
            )
            R.id.nav2NtSung -> setBundleListBook("0B0-bfr9v36LURlhkOU1zcjYtMTA", "nav_2_nt_sung")
            R.id.nav2NtNguoc -> setBundleListBook(
                "0B0-bfr9v36LUYWFoV01weXZIVTQ",
                "nav_2_nt_nguoc"
            )
            R.id.nav2NtHacBang -> setBundleListBook(
                "0B0-bfr9v36LUS3FSc2FpZWpxSGM",
                "nav_2_nt_hacbang"
            )
            R.id.nav2NtSac -> setBundleListBook("0B0-bfr9v36LUWVFXblFjTXBxZDg", "nav_2_nt_sac")
            R.id.nav2NtHai -> setBundleListBook("0B0-bfr9v36LUeHNDblE5LW5YV2M", "nav_2_nt_hai")
            R.id.nav2NtThanhMaiTrucMa -> setBundleListBook(
                "0B0-bfr9v36LURGlTdmVOaU43NVE",
                "nav_2_nt_thanhmaitrucma"
            )
            R.id.nav2NtCamLuyen -> setBundleListBook(
                "0B0-bfr9v36LUSHgyTEpHTlcxUHc",
                "nav_2_nt_camluyen"
            )
            R.id.nav2NtSuDoLuyen -> setBundleListBook(
                "0B0-bfr9v36LUMnl2QUgzUEI5c3M",
                "nav_2_nt_sudoluyen"
            )
            R.id.nav2NtHueynHuyen -> setBundleListBook(
                "0B0-bfr9v36LUeVRxRndBanIyYnM",
                "nav_2_nt_hueynhuyen"
            )
            else -> fragment = FrmQuote()
        }
        replaceFragment()
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment() {
        fragment?.let {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flContent, it)
            fragmentTransaction.commit()
        }
    }

    private fun setBundleListBook(keyURL: String, fileName: String) {
        fragment = FrmListBook()
        val bundle = Bundle()
        bundle.putString("mUrlDataGGDrive", keyURL)
        bundle.putString("fileName", fileName)
        fragment?.arguments = bundle
    }
}
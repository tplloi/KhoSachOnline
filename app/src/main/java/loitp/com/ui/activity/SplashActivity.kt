package loitp.com.ui.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.utilities.LDialogUtil
import com.core.utilities.LUIUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import loitp.com.R

@LogTag("SplashActivity")
@IsFullScreen(false)
class SplashActivity : BaseFontActivity() {
    private var isAnimDone = false
    private var isCheckReadyDone = false
    private var isShowDialogCheck = false

    override fun setLayoutResourceId(): Int {
        return R.layout.act_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LUIUtil.setDelay(2000) {
            isAnimDone = true
            goToHome()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isShowDialogCheck) {
            checkPermission()
        }
    }

    private fun checkPermission() {
        isShowDialogCheck = true
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        isCheckReadyDone = true
                        goToHome()
                    } else {
                        showShouldAcceptPermission()
                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                    isShowDialogCheck = true
                }
            })
            .onSameThread()
            .check()
    }

    private fun goToHome() {
        if (isAnimDone && isCheckReadyDone) {
            LUIUtil.setDelay(2000) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showSettingsDialog() {
        val alertDialog = LDialogUtil.showDialog2(
            context = this,
            title = "Cần quyền truy cập",
            msg = "Vui lòng cấp quyền để có thể dùng ứng dụng",
            button1 = "Cài đặt",
            button2 = "Hủy bỏ",
            onClickButton1 = {
                isShowDialogCheck = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, 101)
            },
            onClickButton2 = {
                onBackPressed()
            },
        )
        alertDialog.setCancelable(false)
    }

    private fun showShouldAcceptPermission() {
        val alertDialog = LDialogUtil.showDialog2(
            context = this,
            title = "Cần quyền truy cập",
            msg = "Vui lòng cấp quyền để có thể dùng ứng dụng",
            button1 = "Okay",
            button2 = "Hủy bỏ",
            onClickButton1 = {
                checkPermission()
            },
            onClickButton2 = {
                onBackPressed()
            })
        alertDialog.setCancelable(false)
    }
}

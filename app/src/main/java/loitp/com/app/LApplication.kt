package loitp.com.app

import com.core.base.BaseApplication
import com.core.common.Constants
import com.core.utilities.LUIUtil
import com.data.ActivityData
import com.data.AdmobData
import loitp.com.R

//TODO crash neu ko co ket noi
//TODO admob, full ad activity
//TODO check messenger trong basemaster
//TODO add pkg name valid basemaster
//TODO ic launcher
//TODO dark mode
//TODO drive valid
//TODO dark mode
//TODO gallery
class LApplication : BaseApplication() {
    override fun onCreate() {
//        if (Const.Config.DEVELOPER_MODE) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
//        }
        super.onCreate()

        //config admob id
        AdmobData.instance.idAdmobFull = getString(R.string.str_f)

        //config activity transition default
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT

        //config font
        LUIUtil.fontForAll = Constants.FONT_PATH
    }
}
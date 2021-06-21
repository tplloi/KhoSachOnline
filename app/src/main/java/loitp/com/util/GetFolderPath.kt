package loitp.com.util

import android.content.Context
import android.os.Environment
import java.io.File

object GetFolderPath {
    var nameFolder = "zBookKK"
    var folderPath: String? = null

    fun getFolderPath(context: Context): String? {
        if (isSdPresent) {
            try {
                val sdPath =
                    File(Environment.getExternalStorageDirectory().absolutePath + "/$nameFolder")
                if (!sdPath.exists()) {
                    val result = sdPath.mkdirs()
                    folderPath = sdPath.absolutePath
                } else if (sdPath.exists()) {
                    folderPath = sdPath.absolutePath
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            folderPath = Environment.getExternalStorageDirectory().path + "/$nameFolder/"
        } else {
            try {
                val cacheDir = File(context.cacheDir, "$nameFolder/")
                if (!cacheDir.exists()) {
                    val result = cacheDir.mkdirs()
                    folderPath = cacheDir.absolutePath
                } else if (cacheDir.exists()) {
                    folderPath = cacheDir.absolutePath
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return folderPath
    }

    private val isSdPresent: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}
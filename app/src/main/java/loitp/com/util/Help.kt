package loitp.com.util

import android.content.*
import android.os.Build
import com.core.utilities.LAppResource
import com.views.LToast
import loitp.com.R
import loitp.com.util.GetFolderPath.getFolderPath
import java.io.*
import java.util.*

object Help {
    fun readTxtFromRawFolder(nameOfRawFile: Int): String {
        val inputStream: InputStream =
            LAppResource.application.resources.openRawResource(nameOfRawFile)
        val byteArrayOutputStream = ByteArrayOutputStream()
        var i: Int
        try {
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOutputStream.toString()
    }

    fun readTxtFromFolder(fileName: String): String {
        val path = getFolderPath(LAppResource.application) + fileName
        val txtFile = File(path)
        val text = StringBuilder()
        try {
            val reader = BufferedReader(FileReader(txtFile))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                text.append(line).append('\n')
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return text.toString()
    }

    fun getRandomQuote(): String {
        val inputStream: InputStream
        val arrStr: Array<String>
        var str = ""
        try {
            inputStream = LAppResource.application.assets.open("quote.txt")
            val buffer = ByteArray(inputStream.available())
            val result = inputStream.read(buffer)
            inputStream.close()
            val s = String(buffer)
            arrStr = s.split("###").toTypedArray()
            val ran = getRandomNumber(arrStr.size)
            str = arrStr[ran]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return str
    }

    fun sendEmail(context: Context?) {
        context?.let { c ->
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(c.getString(R.string.myEmailDev)))
            i.putExtra(Intent.EXTRA_SUBJECT, c.getString(R.string.mail_subject_support))
            i.putExtra(Intent.EXTRA_TEXT, c.resources.getString(R.string.mail_text_support))
            try {
                c.startActivity(Intent.createChooser(i, "Send mail via:"))
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    fun sendEmailRequestBook(context: Context?) {
        context?.let { c ->
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(c.resources.getString(R.string.myEmailDev))
            )
            i.putExtra(Intent.EXTRA_SUBJECT, c.resources.getString(R.string.mail_subject))
            i.putExtra(
                Intent.EXTRA_TEXT,
                c.resources.getString(R.string.mail_text) + deviceInfo
            )
            try {
                c.startActivity(Intent.createChooser(i, "Send mail via:"))
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    private val deviceInfo: String
        get() {
            var info = ""
            info = """
        
        OS Version: ${System.getProperty("os.version")}
        API Level: ${Build.VERSION.SDK}
        Device: ${Build.DEVICE}
        Model: ${Build.MODEL}
        Product: ${Build.PRODUCT}
        Brand: ${Build.BRAND}
        Display: ${Build.DISPLAY}
        Hardware: ${Build.HARDWARE}
        Manufacture: ${Build.MANUFACTURER}
        Serial: ${Build.SERIAL}
        User: ${Build.USER}
        Host: ${Build.HOST}
        """.trimIndent()
            return info
        }

    @JvmStatic
    fun getRandomNumber(length: Int): Int {
        val x: Int
        val r = Random()
        x = r.nextInt(length)
        return x
    }

    fun setClipboard(text: String) {
        val clipboard =
            LAppResource.application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copy", text)
        clipboard.setPrimaryClip(clip)
        LToast.showLongInformation(
            """${LAppResource.getString(R.string.copied)}$text""".trimIndent(),
        )
    }

    fun saveIntToData(key: String, value: Int) {
        val editor: SharedPreferences.Editor =
            LAppResource.application.getSharedPreferences(Const.PREFERENCES_FILE_NAME, 0).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getIntFromData(key: String): Int {
        val prefs: SharedPreferences =
            LAppResource.application.getSharedPreferences(Const.PREFERENCES_FILE_NAME, 0)
        return prefs.getInt(key, 0)
    }

    fun saveBooleanToData(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor =
            LAppResource.application.getSharedPreferences(Const.PREFERENCES_FILE_NAME, 0).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    @JvmStatic
    fun getBooleanFromData(key: String): Boolean {
        val prefs: SharedPreferences =
            LAppResource.application.getSharedPreferences(Const.PREFERENCES_FILE_NAME, 0)
        return prefs.getBoolean(key, true)
    }
}

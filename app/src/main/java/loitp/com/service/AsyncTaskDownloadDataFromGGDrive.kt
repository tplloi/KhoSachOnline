package loitp.com.service

import android.os.AsyncTask
import com.core.utilities.LLog
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

class AsyncTaskDownloadDataFromGGDrive(
    private val mPath: String,
    private val onPreExecute: ((Unit) -> Unit),
    private val onProgressUpdate: ((String) -> Unit),
    private val onSuccess: ((Unit) -> Unit),
    private val onFailed: ((Unit) -> Unit)
) : AsyncTask<String, String, String>() {
    private val logTag = "loitpp" + javaClass.simpleName
    private var runComplete = true

    private fun log(msg: String) {
        LLog.d(logTag, msg)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke(Unit)
    }

    override fun doInBackground(vararg url: String): String? {
        var count: Int
        try {
            val mUrl = URL(url[0])
            log("doInBackground mUrl $mUrl")
            val connection = mUrl.openConnection()
            connection.connect()
            val lengthOfFile = connection.contentLength
            log("doInBackground lengthOfFile $lengthOfFile")
            val input: InputStream = BufferedInputStream(mUrl.openStream(), 10 * 1024)
            val path = mPath
            log("doInBackground path $path")
            val output: OutputStream = FileOutputStream(path)
            val data = ByteArray(1024)
            var total: Long = 0
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                publishProgress("" + (total * 100 / lengthOfFile).toInt())
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
        } catch (e: Exception) {
            log("doInBackground e $e")
            runComplete = false
            e.printStackTrace()
        }
        return null
    }

    override fun onProgressUpdate(vararg progress: String) {
        onProgressUpdate.invoke(progress[0])
    }

    override fun onPostExecute(file_url: String?) {
        log("onPostExecute runComplete $runComplete")
        if (runComplete) {
            onSuccess.invoke(Unit)
        } else {
            onFailed.invoke(Unit)
        }
    }
}
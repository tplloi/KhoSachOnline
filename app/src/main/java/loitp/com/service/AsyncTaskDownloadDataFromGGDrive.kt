package loitp.com.service

import android.os.AsyncTask
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
    private var runComplete = true

    override fun onPreExecute() {
        super.onPreExecute()
        onPreExecute.invoke(Unit)
    }

    override fun doInBackground(vararg url: String): String? {
        var count: Int
        try {
            val mUrl = URL(url[0])
            val connection = mUrl.openConnection()
            connection.connect()
            val lengthOfFile = connection.contentLength
            val input: InputStream = BufferedInputStream(mUrl.openStream(), 10 * 1024)
            val path = mPath
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
            runComplete = false
            e.printStackTrace()
        }
        return null
    }

    override fun onProgressUpdate(vararg progress: String) {
        onProgressUpdate.invoke(progress[0])
    }

    override fun onPostExecute(file_url: String?) {
        if (runComplete) {
            onSuccess.invoke(Unit)
        } else {
            onFailed.invoke(Unit)
        }
    }
}
package loitp.com.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
open class Book {
    @SerializedName("aut")
    @Expose
    var aut: String? = null

    @SerializedName("rq")
    @Expose
    var rq: String? = null

    @SerializedName("thumb")
    @Expose
    var thumb: String? = null

    @SerializedName("tit")
    @Expose
    var tit: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null
}

package loitp.com.model

import androidx.annotation.Keep
import com.core.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class OneChap : BaseModel() {
    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null
}

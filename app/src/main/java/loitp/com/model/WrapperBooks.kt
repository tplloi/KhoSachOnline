package loitp.com.model

import androidx.annotation.Keep
import com.core.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class WrapperBooks : BaseModel() {
    @SerializedName("books")
    @Expose
    val books: Books? = null

    @SerializedName("stat")
    @Expose
    val stat: String? = null
}

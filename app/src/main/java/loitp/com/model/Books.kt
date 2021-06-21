package loitp.com.model

import androidx.annotation.Keep
import com.core.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class Books : BaseModel() {
    @SerializedName("book")
    @Expose
    val book: List<Book>? = null
}

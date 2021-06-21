package loitp.com.model

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class BookFavourite : Book() {

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("pos")
    @Expose
    var pos = 0

    @SerializedName("poswv")
    @Expose
    var poswv = 0

}

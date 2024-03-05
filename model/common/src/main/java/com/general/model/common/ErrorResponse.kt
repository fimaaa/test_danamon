package com.general.model.common
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "code")
    @SerializedName(value = "code")
    var code: Int? = null,
    @Json(name = "code_type")
    @SerializedName(value = "code_type")
    val codeType: String? = null,
    @Json(name = "code_message")
    @SerializedName(value = "code_message")
    var codeMessage: String? = null,
    @Json(name = "message")
    @SerializedName(value = "message")
    val message: String? = null,
    @Json(name = "data")
    @SerializedName(value = "data")
    val data: Any? = null
) : Parcelable {
    // Write object data to Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(code)
        parcel.writeString(codeType)
        parcel.writeString(codeMessage)
        parcel.writeString(message)
        // Writing `data` object is a bit complex, so you can handle it based on the actual data type.
        // Here's an example assuming `data` is of type `String`.
        parcel.writeString(data as? String)
    }

    // Read object data from Parcel
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorResponse> {
        // Create object from Parcel
        override fun createFromParcel(parcel: Parcel): ErrorResponse {
            return ErrorResponse(
                code = parcel.readValue(Int::class.java.classLoader) as? Int,
                codeType = parcel.readString(),
                codeMessage = parcel.readString(),
                message = parcel.readString(),
                data = parcel.readString()
            )
        }

        override fun newArray(size: Int): Array<ErrorResponse?> {
            return arrayOfNulls(size)
        }
    }
}
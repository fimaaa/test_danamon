package com.general.model.common.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    @SerializedName(value = "id") @Json(name = "id") val memberId: Int,
    @SerializedName(value = "username") @Json(name = "username") val userName: String = "",
    @SerializedName(value = "fullname") @Json(name = "fullname") val name: String = "",
    @SerializedName(value = "courier_type") @Json(name = "courier_type") val userType: String = "",
    @SerializedName(value = "phone_number") @Json(name = "phone_number") val phone: String = "",
    @SerializedName(value = "token") @Json(name = "token") val apiToken: String,
    @SerializedName(value = "refresh_token") @Json(name = "refresh_token") val refreshToken: String,
    @SerializedName(value = "password") @Json(name = "password") val password: String
) : Parcelable

@Entity(tableName = "member")
data class MemberLocal(
    @PrimaryKey(autoGenerate = true)
    @SerializedName(value = "id") val memberId: Int = 0,
    @SerializedName(value = "username") val userName: String,
    @SerializedName(value = "full_name") val name: String,
    @SerializedName(value = "courier_type") val userType: String,
    @SerializedName(value = "phone_number") val phone: String,
    @SerializedName(value = "token") val apiToken: String = "",
    @SerializedName(value = "refresh_token") val refreshToken: String = "",
    @SerializedName(value = "password") val password: String
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(memberId)
        parcel.writeString(userName)
        parcel.writeString(name)
        parcel.writeString(userType)
        parcel.writeString(phone)
        parcel.writeString(apiToken)
        parcel.writeString(refreshToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemberLocal> {
        override fun createFromParcel(parcel: Parcel): MemberLocal {
            return MemberLocal(
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: ""
            )
        }

        override fun newArray(size: Int): Array<MemberLocal?> {
            return arrayOfNulls(size)
        }
    }
}

fun MemberLocal.toMember() =
    Member(memberId, userName, name, userType, phone, apiToken, refreshToken, password)

fun Member.toLocal() =
    MemberLocal(memberId, userName, name, userType, phone, apiToken, refreshToken, password)
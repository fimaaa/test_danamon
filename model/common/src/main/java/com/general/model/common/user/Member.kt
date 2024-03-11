package com.general.model.common.user

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class CreateMemberData(
    @SerializedName("username") @Json(name = "username") val username: String,
    @SerializedName("fullname") @Json(name = "fullname") val fullname: String,
    @SerializedName("password") @Json(name = "password") val password: String,
    @SerializedName("member_type") @Json(name = "member_type") val memberType: String = "user",
    @SerializedName("phone_number") @Json(name = "phone_number") val phoneNumber: String? = "",
    @SerializedName("email") @Json(name = "email") val email: String? = "",
    @SerializedName("photo_member") @Json(name = "photo_member") val memberPhoto: String? = ""
)

@Parcelize
data class Member(
    @SerializedName("id") @Json(name = "id") val id: String = "",
    @SerializedName("username") @Json(name = "username") val username: String = "",
    @SerializedName("fullname") @Json(name = "fullname") val fullname: String = "",
    @SerializedName("member_type") @Json(name = "member_type") val memberType: String = "",
    @SerializedName("is_suspend") @Json(name = "is_suspend") val isSuspend: Boolean = false,
    @SerializedName("created_at") @Json(name = "created_at") val createdAt: String = "",
    @SerializedName("updated_at") @Json(name = "updated_at") val updatedAt: String = "",
    @SerializedName("token_broadcast") @Json(name = "token_broadcast") val tokenBroadcast: String = "",
    @SerializedName("last_login") @Json(name = "last_login") val lastLogin: String = "",
    @SerializedName("id_device") @Json(name = "id_device") val deviceId: String = "",
    @SerializedName("phone_number") @Json(name = "phone_number") val phoneNumber: String = "",
    @SerializedName("email") @Json(name = "email") val email: String = "",
    @SerializedName("photo_member") @Json(name = "photo_member") val memberPhoto: String = "",
    @SerializedName(value = "token") @Json(name = "token") val apiToken: String = "",
    @SerializedName(value = "refresh_token") @Json(name = "refresh_token") val refreshToken: String = ""
) : Parcelable

@Parcelize
data class MemberFind(
    @SerializedName(value = "value") @Json(name = "value") val filter: Member,
    @SerializedName(value = "sort_by") @Json(name = "sort_by") val sortBy: String
) : Parcelable

@Entity(tableName = "member")
data class MemberLocal(
    @PrimaryKey()
    @SerializedName(value = "id") val memberId: String = "",
    @SerializedName(value = "username") val userName: String,
    @SerializedName(value = "full_name") val name: String,
    @SerializedName(value = "courier_type") val userType: String,
    @SerializedName(value = "phone_number") val phone: String,
    @SerializedName(value = "token") val apiToken: String = "",
    @SerializedName(value = "refresh_token") val refreshToken: String = "",
    @SerializedName(value = "password") val password: String
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(memberId)
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
                parcel.readString() ?: "",
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

// fun MemberLocal.toMember() =
//    Member(memberId, userName, name, userType, phone, apiToken, refreshToken, password)
//
// fun Member.toLocal() =
//    MemberLocal(memberId, userName, name, userType, phone, apiToken, refreshToken, password)
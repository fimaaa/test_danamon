package com.general.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.general.local.common.dao.BaseDao
import com.general.model.common.user.MemberLocal

@Dao
interface MemberDao : BaseDao<MemberLocal> {
    @Query("SELECT * FROM member LIMIT :pageSize OFFSET ((:page) * :pageSize)")
    suspend fun fetchListMembers(pageSize: Int, page: Int): List<MemberLocal>

    @Query("SELECT * FROM member")
    suspend fun fetchAllMembers(): List<MemberLocal>

    @Query("SELECT * FROM member WHERE memberId=:memberId")
    suspend fun fetchMember(memberId: String): MemberLocal?

    @Query("SELECT * FROM member WHERE phone = :phone AND password = :password")
    suspend fun postLogin(phone: String, password: String): MemberLocal?

    @Query("SELECT * FROM member WHERE phone = :phone")
    fun getMemberByPhoneNumber(phone: String): MemberLocal?

    @Update
    suspend fun updateUser(member: MemberLocal): Int

    @Query("SELECT COUNT(*) FROM member WHERE phone = :phoneNumber")
    fun checkPhoneNumberExist(phoneNumber: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun registerMember(user: MemberLocal): Long

    @Query("DELETE FROM member")
    fun clearAppPages()
}
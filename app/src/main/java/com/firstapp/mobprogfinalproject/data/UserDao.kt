
package com.firstapp.mobprogfinalproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    // Function for Lanz (User Account & Authentication)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    // Function for Lanz (User Account & Authentication)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE users SET fullName = :fullName, mobileNumber = :mobileNumber WHERE userId = :userId")
    suspend fun updateUser(userId: Int, fullName: String, mobileNumber: String)

}

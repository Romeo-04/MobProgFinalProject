
package com.firstapp.mobprogfinalproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VoucherDao {

    // Function for Marcus (Deals/Vouchers)
    @Query("SELECT * FROM vouchers WHERE isActive = 1")
    fun getActiveVouchers(): Flow<List<Voucher>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vouchers: List<Voucher>)

    @Query("SELECT COUNT(*) FROM vouchers")
    suspend fun getVoucherCount(): Int

}


package com.firstapp.mobprogfinalproject.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VoucherDao {

    // Function for Marcus (Deals/Vouchers)
    @Query("SELECT * FROM vouchers WHERE isActive = 1")
    fun getActiveVouchers(): Flow<List<Voucher>>

}

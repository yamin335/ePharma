package com.rtchubs.pharmaerp.local_db.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rtchubs.pharmaerp.models.OrderProduct

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "product") val product: OrderProduct,
    @ColumnInfo(name = "quantity") val quantity: Int?
)
package com.example.expensemanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses_Table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val title:String,
    val amount:Double,
    val date:String,
    val category:String,
    val type:String,
    val totalBalance:Double,
)



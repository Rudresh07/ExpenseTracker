package com.example.expensemanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notification_Table")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val message:String,
    val timestamp: Long = System.currentTimeMillis()
)

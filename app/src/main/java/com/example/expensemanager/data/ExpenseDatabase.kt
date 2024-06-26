package com.example.expensemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensemanager.data.dao.ExpenseDao
import com.example.expensemanager.data.dao.NotificationDao
import com.example.expensemanager.data.model.ExpenseEntity
import com.example.expensemanager.data.model.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class, NotificationEntity::class], version = 1)
abstract class ExpenseDatabase :RoomDatabase() {
   abstract fun expenseDao(): ExpenseDao
   abstract fun notificationDao(): NotificationDao



   companion object{
       const val DATABASE_NAME="expense_database"

       @JvmStatic
       fun getDatabase(context: Context): ExpenseDatabase {
           return Room.databaseBuilder(
               context,
               ExpenseDatabase::class.java,
               DATABASE_NAME
           ).addCallback(object : Callback(){
               override fun onCreate(db: SupportSQLiteDatabase) {
                   super.onCreate(db)
                   InitBasicData(context)
               }

               fun InitBasicData(context: Context){
                   CoroutineScope(Dispatchers.IO).launch {
                       val dao = getDatabase(context).expenseDao()
                       val dao1 = getDatabase(context).notificationDao()
                   }
               }
           }).build()
       }
   }
}
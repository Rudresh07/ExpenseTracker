package com.example.expensemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensemanager.data.model.BalanceEntry
import com.example.expensemanager.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT date, totalBalance FROM expenses_Table ORDER BY date ASC")
   suspend fun getAllBalanceEntries(): List<BalanceEntry>


   @Query("SELECT * FROM expenses_Table WHERE type ='Expense' ORDER BY amount DESC ")
   fun getAllTransaction(): Flow<List<ExpenseEntity>>


    @Query("SELECT * FROM expenses_Table  ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(CASE WHEN type = 'Income' THEN amount ELSE -amount END) AS totalBalance FROM expenses_Table")
    suspend fun getTotalBalance(): Double?

    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntity)


}
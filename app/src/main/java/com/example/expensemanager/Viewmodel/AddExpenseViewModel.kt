package com.example.expensemanager.Viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensemanager.data.ExpenseDatabase
import com.example.expensemanager.data.dao.ExpenseDao
import com.example.expensemanager.data.model.ExpenseEntity

class AddExpenseViewModel(val dao: ExpenseDao):ViewModel() {


    suspend fun addExpense(expenseEntity: ExpenseEntity):Boolean{
        try {
            dao.insertExpense(expenseEntity)
            return true}
        catch (ex:Throwable){
            return false
        }
    }

    suspend fun getTotalBalance(): Double? {
        return try {
            dao.getTotalBalance()
        } catch (ex: Throwable) {
            null
        }
    }

    class AddExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
                val dao = ExpenseDatabase.getDatabase(context).expenseDao()
                return AddExpenseViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}
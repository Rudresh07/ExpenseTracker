package com.example.expensemanager.Viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.R
import com.example.expensemanager.data.ExpenseDatabase
import com.example.expensemanager.data.dao.ExpenseDao
import com.example.expensemanager.data.model.BalanceEntry
import com.example.expensemanager.data.model.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewmodel(val dao:ExpenseDao) : ViewModel() {


    val expense = dao.getAllExpenses()
    val spending = dao.getAllTransaction()

    fun getItemIcon(item: ExpenseEntity): Int {
        if (item.category == "Paypal"){
            return R.drawable.ic_paypal
        }
        else if (item.category == "Youtube"){
            return R.drawable.ic_youtube
        }
        else if (item.category == "Subscription"){
            return R.drawable.subscription
        }

        else if (item.category == "Starbucks"){
            return R.drawable.ic_starbucks
        }
        else if (item.category == "Upwork"){
            return R.drawable.ic_upwork
        }

        else if (item.category == "Education"){
            return R.drawable.education
        }

        else if (item.category == "Online Shopping"){
            return R.drawable.online_shoping
        }

        else if (item.category == "Offline Shopping"){
            return R.drawable.offline_shoping
        }

        else if (item.category == "Hospital"){
            return R.drawable.medicine
        }

        else if (item.category == "Bill Payment"){
            return R.drawable.bills
        }
        else if (item.category == "Grocery"){
            return R.drawable.grocery
        }
        else if (item.category == "Loan"){
            return R.drawable.loan
        }
        else if (item.category == "Rent"){
            return R.drawable.rent
        }
        else if (item.category == "Credit Card"){
            return R.drawable.credit_card
        }
        else if (item.category == "Money Transfer"){
            return R.drawable.money_transfer
        }
        else if (item.category == "EMI Payment"){
            return R.drawable.bills
        }

        else if (item.category == "Salary"){
            return R.drawable.salary_pay
        }

        return R.drawable.other
    }




    fun getBalance(list: List<ExpenseEntity>): String {
        var total = 0.0
        for (expense in list){
            if (expense.type == "Expense"){
                total+= expense.amount
            }
        }

        return "₹ ${total}"
    }

    fun getTotalExpense(list: List<ExpenseEntity>): String {

        var balance = 0.0
        for (expense in list){
            if (expense.type == "Income"){
                balance += expense.amount
            }
            else{
                balance -= expense.amount
            }
        }

        return "₹ ${balance}"

    }

    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        for (expense in list){
            if (expense.type == "Income"){
                totalIncome += expense.amount
            }

        }

        return "₹ ${totalIncome}"
    }

    private val _balanceEntries = MutableStateFlow<List<BalanceEntry>>(emptyList())
    val balanceEntries: Flow<List<BalanceEntry>> get()= _balanceEntries

    init {
        fetchBalanceEntries()
    }

    fun fetchBalanceEntries() {
        viewModelScope.launch {
            _balanceEntries.value = dao.getAllBalanceEntries()
        }
    }







    class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewmodel::class.java)) {
                val dao = ExpenseDatabase.getDatabase(context).expenseDao()
                return HomeViewmodel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}

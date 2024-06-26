package com.example.expensemanager.Viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.data.ExpenseDatabase
import com.example.expensemanager.data.dao.NotificationDao
import com.example.expensemanager.data.model.NotificationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(private val dao: NotificationDao) : ViewModel() {
    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    init {
        viewModelScope.launch {
            _notifications.value = dao.getAllNotifications()
        }
    }

    suspend fun addNotification(message: String): Boolean {
        return try {
            val time = System.currentTimeMillis()
            val notification = NotificationEntity(null,message = message,time)
            dao.insert(notification)
            _notifications.value = dao.getAllNotifications()
            true
        } catch (ex: Throwable) {
            false
        }
    }

    class NotificationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                val dao = ExpenseDatabase.getDatabase(context).notificationDao()
                return NotificationViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

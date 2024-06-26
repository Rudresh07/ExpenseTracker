package com.example.expensemanager.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.expensemanager.R
import com.example.expensemanager.Viewmodel.NotificationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationHandler(private val context: Context, private val viewModel: NotificationViewModel) {

    fun createNotificationChannel() {
        val name = "Expense Channel"
        val descriptionText = "Notifications for expenses"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("expense_channel", name, importance).apply {
            description = descriptionText
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(amount: Double, type: String, totalBalance: MutableState<Double?>, name: String) {
        val notificationId = (System.currentTimeMillis() % 10000).toInt()
        val message = "An $type of ₹$amount for $name has been added. Remaining balance: ₹${totalBalance.value}"

        val builder = NotificationCompat.Builder(context, "expense_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Expense Manager")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }

        // Save notification to the database
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.addNotification(message)
        }
    }
}

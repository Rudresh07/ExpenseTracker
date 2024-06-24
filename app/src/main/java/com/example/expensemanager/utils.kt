package com.example.expensemanager

import java.text.SimpleDateFormat
import java.util.Locale

object utils {

    fun formatDate(dateInMillis:Long): String {

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun parseDateStringToMillis(dateString: String): Long {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormatter.parse(dateString)
        return date?.time ?: 0L
    }

}
package com.example.expensemanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class navigationItem (
    val label: String,
    val icon: ImageVector,
    val route: String
)
val listOfNavItems = listOf(
    navigationItem(
        label = "Home",
        icon = Icons.Filled.Home,
        route = "/home"
    ),
    navigationItem(
        label = "ADD Expense",
        icon = Icons.Filled.Add,
        route = "/add"

    ),
    navigationItem(
        label = "Statistics",
        icon = Icons.Filled.AutoGraph,
        route = "/graph"
    )

)
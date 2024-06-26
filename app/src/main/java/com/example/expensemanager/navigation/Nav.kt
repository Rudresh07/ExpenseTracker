package com.example.expensemanager.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.AddExpense
import com.example.expensemanager.ExpenseGraph
import com.example.expensemanager.HomeScreen
import com.example.expensemanager.Transaction

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOfNavItems.forEach { navigationItem ->
                    NavigationBarItem(selected = currentDestination?.hierarchy?.any{it.route == navigationItem.route} == true,
                        onClick = {
                                  navController.navigate(navigationItem.route) {
                                      popUpTo(navController.graph.findStartDestination().id){
                                          saveState = true

                                      }
                                      launchSingleTop =true
                                      restoreState = true
                                  }
                        },
                        icon = { Icon(imageVector = navigationItem.icon,
                            contentDescription =null ) },
                        label = {
                            Text(text = navigationItem.label)
                        })
                }
            }
        },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) {
        paddingValues ->
        NavHost(navController =navController , startDestination = "/home",
            modifier = Modifier.padding(paddingValues)  )
        {
            composable(route = "/home")
            {
            HomeScreen(navController)
        }

            composable(route = "/add")
            {
                AddExpense(navController)
            }

            composable(route = "/graph")
            {
                ExpenseGraph(navController)
            }

            composable(route = "/transactions")
            {
               // TransactionScreen(navController)
               val intent = Intent(LocalContext.current,Transaction::class.java)
                startActivity(LocalContext.current,intent,null)
            }

        }
    }

}
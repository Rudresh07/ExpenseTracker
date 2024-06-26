package com.example.expensemanager

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.expensemanager.navigation.NavHostScreen
import com.example.expensemanager.ui.theme.ExpenseManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseManagerTheme {
               Surface(modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colorScheme.background) {
                   //permission for notification in android 13 and above

                   val notificationLauncher = rememberLauncherForActivityResult(
                       contract = ActivityResultContracts.RequestPermission()) {

                   }

                   LaunchedEffect (key1 = true){
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                           notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                       }

                   }


                    NavHostScreen()
                }
            }
        }
    }
}

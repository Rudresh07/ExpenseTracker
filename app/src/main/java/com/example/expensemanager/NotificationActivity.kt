package com.example.expensemanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensemanager.Viewmodel.NotificationViewModel
import com.example.expensemanager.data.model.NotificationEntity
import com.example.expensemanager.notification.NotificationHandler
import com.example.expensemanager.ui.theme.ExpenseManagerTheme


class NotificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExpenseManagerTheme {
                NotificationScreen(context = this)
            }
        }
    }
}

@Composable
fun NotificationScreen(context: Context) {
    val viewModel: NotificationViewModel = viewModel(factory = NotificationViewModel.NotificationViewModelFactory(context))
    val notificationHandler = NotificationHandler(context, viewModel)

    notificationHandler.createNotificationChannel()

    val notifications = viewModel.notifications.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_back_black),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterStart).clickable {
                            val intent = Intent(context, MainActivity::class.java)
                            ContextCompat.startActivity(context, intent, null)
                        }
                    )
                    ExpenseTextView(
                        text = "Notifications",
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().padding(top = 16.dp, start = 8.dp, end = 8.dp)
                    .constrainAs(list) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                items(notifications.value) { notification ->
                    NotificationItem(notification)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)


    ) {
        Text(
            text = notification.message,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

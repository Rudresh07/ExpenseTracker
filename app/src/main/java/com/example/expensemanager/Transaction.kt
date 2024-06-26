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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat.startActivity
import com.example.expensemanager.Viewmodel.HomeViewmodel
import com.example.expensemanager.data.model.ExpenseEntity
import com.example.expensemanager.ui.theme.ExpenseManagerTheme

class Transaction : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseManagerTheme {
                // Remove the Surface from here
                TransactionsScreen(context = this)
            }
        }
    }
}

@Composable
fun TransactionsScreen(context: Context) {
    val viewmodel: HomeViewmodel =
        HomeViewmodel.HomeViewModelFactory(LocalContext.current).create(HomeViewmodel::class.java)

    // Surface component is here
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
                    modifier = Modifier.clickable {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(context, intent, null)
                    })

                ExpenseTextView(
                    text = "Transactions",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            val state = viewmodel.expense.collectAsState(initial = emptyList())

            Transactionslist(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
                list = state.value,
                viewmodel
            )
        }
    }
}

@Composable
fun Transactionslist(modifier: Modifier, list: List<ExpenseEntity>, viewmodel: HomeViewmodel) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        items(list) { item ->
            EachTransactionItems(
                title = item.title,
                amount = with(item.amount.toString()) { // Use with block for string manipulation
                    if (item.type == "Income") {
                        "+ ₹ $this" // Prepend "+" and rupee symbol for income
                    } else {
                        "- ₹ $this" // Prepend "-" and rupee symbol for expense
                    }
                },
                icon = viewmodel.getItemIcon(item),
                date = item.date.toString(),
                color = if (item.type == "Income") Color.Green else Color.Red
            )
        }
    }
}

@Composable
fun EachTransactionItems(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))

            Column(modifier = Modifier
                .fillMaxWidth(0.7f)) {
                ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                ExpenseTextView(text = date, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

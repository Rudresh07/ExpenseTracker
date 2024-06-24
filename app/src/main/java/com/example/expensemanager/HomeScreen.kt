package com.example.expensemanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.Viewmodel.HomeViewmodel
import com.example.expensemanager.data.model.ExpenseEntity
import com.example.expensemanager.ui.theme.Zinc


@Composable
fun HomeScreen(navController:NavController){
    val viewmodel:HomeViewmodel =
        HomeViewmodel.HomeViewModelFactory(LocalContext.current).create(HomeViewmodel::class.java)
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize() ) {
            val(nameRow, list, card,topBar, add) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            
            Box (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
                 {
                Column(modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 20.dp)) {
                    Text(text = "Hello Rudy", fontSize = 16.sp, color = Color.White)
                    Text(text = "This is an Expense tracker App", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,)
                }
                     
                     Image(painter = painterResource(id = R.drawable.ic_notification),
                         contentDescription = null,
                         modifier = Modifier.align(Alignment.CenterEnd))
            }
            
            val state = viewmodel.expense.collectAsState(initial = emptyList())

            val expenses = viewmodel.getTotalExpense(state.value)
            val income = viewmodel.getTotalIncome(state.value)
            val balance = viewmodel.getBalance(state.value)

            CardItem(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, expenses,income,balance
            )

            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }, list = state.value,
                viewmodel
            )


        }
    }
}

@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expense: String){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)
        ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f))
        {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Total Balance",fontSize = 16.sp, color = Color.White)
                Text(text = balance
                    ,fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                    ,color = Color.White)
            }
            Image(painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd))
        }

        Box(modifier= Modifier
            .fillMaxWidth()
            .weight(1f),
            ) {

            RowItem(modifier =Modifier.align(Alignment.CenterStart) ,
                title ="Income" ,
                amount = income,
                image =R.drawable.ic_income )

            RowItem(modifier =Modifier.align(Alignment.CenterEnd) ,
                title ="Expense" ,
                amount =expense ,
                image = R.drawable.ic_expense)

        }

    }
}

@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>,viewmodel: HomeViewmodel)
{
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item { Box(modifier = Modifier.fillMaxWidth())
        {
            ExpenseTextView(text = "Recent Transaction", fontSize = 20.sp)
            ExpenseTextView(text = "See All",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterEnd))
        } }

       items(list){item ->
           TransactionItem(title = item.title,
               amount = item.amount.toString(),
               icon = viewmodel.getItemIcon(item),
               date = item.date.toString(),
               color = if (item.type == "Income") Color.Green else Color.Red
           )
       }

    }
}

@Composable
fun TransactionItem(title: String,
                    amount: String,
                    icon:Int,
                    date: String,
                    color: Color)
{
Box (modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 8.dp)){

    Row {
        Image(painter = painterResource(id = icon), contentDescription = null,
            modifier = Modifier.size(50.dp))
        Spacer(modifier = Modifier.size(8.dp))
        
        Column {
            ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            ExpenseTextView(text = date, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
    ExpenseTextView(text = amount, fontSize = 20.sp,
        modifier = Modifier.align(Alignment.CenterEnd),
        color = color,
        fontWeight = FontWeight.Bold)

}
}

@Composable
fun RowItem(modifier: Modifier,title: String, amount: String,image:Int)
{
    Column(modifier) {

        Row {
            Image(painter = painterResource(id = image),
                contentDescription =null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, fontSize = 16.sp, color = Color.White)

        }

        ExpenseTextView(text = amount, fontSize = 17.sp, color = Color.White)
    }
}


@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())
}


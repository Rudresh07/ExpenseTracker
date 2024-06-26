@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expensemanager

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.Viewmodel.AddExpenseViewModel
import com.example.expensemanager.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddExpense(navController:NavController) {
    val viewModel = AddExpenseViewModel.AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
   val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }.align(Alignment.CenterStart)
                   // modifier = Modifier.align(Alignment.CenterStart)
                )


                ExpenseTextView(
                    text = "Add Expense", fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )

                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }



            AddExpenseCard(modifier = Modifier
                .padding(top = 32.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                navController
            )

        }

    }

}




@Composable
fun AddExpenseCard(
    modifier: Modifier,
    navController: NavController,
    viewModel: AddExpenseViewModel = viewModel(factory = AddExpenseViewModel.AddExpenseViewModelFactory(LocalContext.current))
) {
    val totalBalance = remember { mutableStateOf<Double?>(null) }

    // Coroutine to fetch total balance
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val balance = viewModel.getTotalBalance()
            withContext(Dispatchers.Main) {
                totalBalance.value = balance
            }
        }
    }

    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf("--Choose Category--") }
    val type = remember { mutableStateOf("--Choose Type--") }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ExpenseTextView(text = "Name", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))

        ExpenseTextView(text = "Amount", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = amount.value, onValueChange = { amount.value = it }, modifier = Modifier.fillMaxWidth(),  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.size(8.dp))

        // Date using datepicker dialog
        ExpenseTextView(text = "Date", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        (if (date.value == 0L) "" else utils.formatDate(date.value))?.let {
            OutlinedTextField(
                value = it,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dateDialogVisibility.value = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Black,
                    disabledTextColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.size(8.dp))

        // Dropdown menu for category
        ExpenseTextView(text = "Category", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(
            listOf(
                "--Choose Category--", "Subscription", "Upworks", "Salary","Bills Payment",
                "Starbucks", "Education", "Hospital", "Online Shopping", "Offline Shopping", "Grocery","EMI Payment","Credit Card",
                "Money Transfer","Rent","Loan","Other"
            ),
            onItemSelected = { category.value = it }
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Dropdown menu for type
        ExpenseTextView(text = "Type", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(
            listOf("--Choose Type--", "Income", "Expense"),
            onItemSelected = { type.value = it }
        )
        Spacer(modifier = Modifier.size(8.dp))

        fun validateAndSubmitExpense() {
            if (category.value == "--Choose Category--" || type.value == "--Choose Type--") {
                errorMessage.value = "Please select a valid category and type"
            } else {
                errorMessage.value = ""
                val expenseAmount = amount.value.toDoubleOrNull() ?: 0.0
                val formattedDate = utils.formatDate(date.value)
                val expenseType = type.value

                val currentTotalBalance = totalBalance.value ?: 0.0
                val updatedTotalBalance = if (expenseType == "Income") {
                    currentTotalBalance + expenseAmount
                } else {
                    currentTotalBalance - expenseAmount

                }
                Log.d("TAG", "validateAndSubmitExpense: $currentTotalBalance")

                val model = ExpenseEntity(
                    id = null,
                     name.value,
                    amount = expenseAmount,
                    date = formattedDate,
                    category = category.value,
                    type = expenseType,
                    totalBalance = updatedTotalBalance
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val isSuccess = viewModel.addExpense(model)
                    if (isSuccess) {
                        withContext(Dispatchers.Main) {
                            totalBalance.value = updatedTotalBalance
                            navController.popBackStack()
                        }
                    }
                }
            }
        }

        Button(onClick = { validateAndSubmitExpense() }, modifier = Modifier.fillMaxWidth()) {
            ExpenseTextView(text = "Add Expense", fontSize = 14.sp, color = Color.White)
        }
    }

    if (dateDialogVisibility.value) {
        DatePicker(
            onDateSelected = {
                date.value = it
                dateDialogVisibility.value = false
            },
            onDismiss = { dateDialogVisibility.value = false }
        )
    }
}




 @Composable
 fun DatePicker(onDateSelected: (date:Long) -> Unit,
                onDismiss : ()->Unit)
 {

     val datePickerState = rememberDatePickerState()
     val selectedDate = datePickerState.selectedDateMillis ?:0L

     DatePickerDialog(
         onDismissRequest = {onDismiss() },
         confirmButton = { TextButton(onClick = { onDateSelected(selectedDate) }) {
             ExpenseTextView(text = "Confirm")
         }
         },
         dismissButton = {
             TextButton(onClick = { onDateSelected(selectedDate)}) {
                 ExpenseTextView(text = "Dismiss")
             }
         }
     )
     {
         androidx.compose.material3.DatePicker(state = datePickerState)
     }
 }

@Composable
fun ExpenseDropDown(listOfItems: List<String>,onItemSelected:(item:String)->Unit) {
    val expanded = remember {
        mutableStateOf(false)

    }

    val selectedItem = remember {
        mutableStateOf<String>(listOfItems[0])
    }



    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        TextField(value = selectedItem.value, onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(), readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = {}) {

            listOfItems.forEach {
                DropdownMenuItem(text = { ExpenseTextView(text = it) }, onClick = {
                    selectedItem.value = it
                    onItemSelected(it)
                    expanded.value = false

                })
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddExpense() {
    AddExpense(rememberNavController())
}


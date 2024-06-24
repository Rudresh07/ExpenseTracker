package com.example.expensemanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.Viewmodel.HomeViewmodel
import com.example.expensemanager.data.model.BalanceEntry
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.diff.ExtraStore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseGraph(navController: NavController,viewModel: HomeViewmodel = viewModel(factory = HomeViewmodel.HomeViewModelFactory(LocalContext.current))) {

    val balanceEntriesState = viewModel.balanceEntries.collectAsState(initial = emptyList())
    val balanceEntries = balanceEntriesState.value

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, card) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_black),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )

                Text(
                    text = "Statistics",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            ShowGraphCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                balanceEntries = balanceEntries
            )
        }
    }
}

@Composable
fun ShowGraphCard(modifier: Modifier,balanceEntries: List<BalanceEntry>) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 4.dp, end = 4.dp)
            .shadow(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(colorResource(id = R.color.teal_700)), modifier = Modifier.width(96.dp)) {
                Text(text = "Day")
            }
            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(colorResource(id = R.color.teal_700)), modifier = Modifier.width(96.dp)) {
                Text(text = "Week")
            }
            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(colorResource(id = R.color.teal_700)), modifier = Modifier.width(96.dp)) {
                Text(text = "Month")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(4.dp)
        ) {
            ShowGraph(balanceEntries)
        }
    }
}

@Composable
fun ShowGraph(balanceEntries:List<BalanceEntry>) {



    val refreshDataset = remember { mutableStateOf(0) }
    val modelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember { mutableStateListOf<List<FloatEntry>>() }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val scrollState = rememberChartScrollState()

    val dateFormat = "dd/MM/yyyy"


    LaunchedEffect(key1 = balanceEntries) {
        datasetForModel.clear()
        datasetLineSpec.clear()


//        val dataPoints = balanceEntries.map {entry ->
//            val parsedDate = LocalDate.parse(entry.date, DateTimeFormatter.ofPattern(dateFormat))
//            val dateEpochDay = parsedDate.toEpochDay().toFloat()
//            println("Mapping: Date - $parsedDate, Balance - ${entry.totalBalance}")
//            FloatEntry(x = dateEpochDay, y = entry.totalBalance.toFloat())
//        }

        val dataPoints = mutableListOf<FloatEntry>()
        for (entry in balanceEntries) {
            try {
                val parsedDate = LocalDate.parse(entry.date, DateTimeFormatter.ofPattern(dateFormat))
                val dateEpochDay = parsedDate.toEpochDay().toFloat()
            println("Mapping: Date - $parsedDate, Balance - ${entry.totalBalance}")
                dataPoints.add(FloatEntry(x = dateEpochDay, y = entry.totalBalance.toFloat()))
            } catch (e: Exception) {
                // Handle parsing error (optional)
                // e.printStackTrace()  // Uncomment for debugging if needed
            }
        }





        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = Green.toArgb(),
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        listOf(
                            Green.copy(com.patrykandpatrick.vico.core.DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            Green.copy(com.patrykandpatrick.vico.core.DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        )
                    )
                )
            )
        )

        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)
    }

    // Display the LineChart using the modelProducer and datasetLineSpec
    if (datasetForModel.isNotEmpty()) {
        ProvideChartStyle ()
        {

            Chart(chart = LineChart(
                lines = datasetLineSpec
            ),
                chartModelProducer = modelProducer,

                startAxis = rememberStartAxis(
                    title = "Total Balance",
                    tickLength = 0.dp,
                    valueFormatter = {value, _->
                        (value.toInt()).toString()
                    },
                    itemPlacer = AxisItemPlacer.Vertical.default(
                        maxItemCount = 8
                    )
                ),

                bottomAxis = rememberBottomAxis(
                    title = "Date",
                    tickLength = 0.dp,
                    valueFormatter = { value, _ ->
                        val date = LocalDate.ofEpochDay(value.toLong())
                        val formattedDate = date.format(DateTimeFormatter.ofPattern(dateFormat))
                        val dateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")
                        date.format(dateTimeFormatter)

                    },

                ),

                chartScrollState = scrollState,
                isZoomEnabled = true,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewGraph() {
    ExpenseGraph(rememberNavController())
}

package com.example.statehosting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.statehosting.ui.theme.StateHostingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateHostingTheme {
                FoodCartScreen(FoodCartViewModel())
            }
        }
    }
}

data class FoodItemData(
    val name: String,
    val price: Int,
    val quantity: Int,
)

class FoodCartViewModel : ViewModel() {

    var foodItems = mutableStateListOf(
        FoodItemData("Paneer Pizza" ,600,1),
        FoodItemData("Maska Bun",100,1),
        FoodItemData("Margeritha Pizzza",600,1),
        FoodItemData("King Burger",200,1),
    )

    fun addItem(index: Int) {
        foodItems[index] = foodItems[index].copy(quantity = foodItems[index].quantity + 1)
    }

    fun removeItem(index:Int){
        if(foodItems[index].quantity > 0){
            foodItems[index] = foodItems[index].copy(quantity = foodItems[index].quantity - 1)
        }
    }
    fun totalBill():Int{
        return foodItems.sumOf { it.price * it.quantity }
    }
}

@Composable
fun FoodCartScreen(viewModel: FoodCartViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center) {
        Text(text="My Food Cart", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Spacer(modifier = Modifier.padding(20.dp))
        viewModel.foodItems.forEachIndexed{index,item->
            FoodItem(
                name = item.name,
                price = item.price,
                quantity = item.quantity,
                onAddClick = {viewModel.addItem(index)},
                onRemoveClick = {viewModel.removeItem(index)}
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Total Bill: ${viewModel.totalBill()}" , fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}


@Composable
fun FoodItem(
    name:String,
    price:Int,
    quantity:Int,
    onAddClick:()->Unit,
    onRemoveClick:()->Unit, 
){
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = name)
            Text(text = "₹ $price")

        }
        Button(onClick = onRemoveClick) {
            Text(text = "-")
        }
        Spacer(modifier = Modifier.padding(8.dp))

        Text(text = "$quantity")

        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = onAddClick) {
            Text(text = "+")
        }


    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StateHostingTheme {
        FoodCartScreen(FoodCartViewModel())
    }
}

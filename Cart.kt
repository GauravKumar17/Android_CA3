
package com.example.statehosting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

data class FoodItem(
    val name: String,
    val price: Int
)


data class CCartItem(
    val food: FoodItem,
    val quantity: Int
)

class FoodViewModel : ViewModel() {

    var cartItems by mutableStateOf(
        listOf(
            CCartItem(FoodItem("Pizza", 600), 1),
            CCartItem(FoodItem("Burger", 200), 1),
            CCartItem(FoodItem("Pasta", 150), 1)
        )
    )
        private set

    fun increase(index: Int) {
        cartItems = cartItems.toMutableList().also {
            it[index] = it[index].copy(
                quantity = it[index].quantity + 1
            )
        }
    }

    fun decrease(index: Int) {
        if (cartItems[index].quantity > 0) {
            cartItems = cartItems.toMutableList().also {
                it[index] = it[index].copy(
                    quantity = it[index].quantity - 1
                )
            }
        }
    }
}

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val vm: FoodViewModel = viewModel()
            FoodCartScreen(vm)
        }
    }
}

@Composable
fun FoodCartScreen(vm: FoodViewModel) {

    Column(modifier = Modifier.padding(20.dp)) {

        Text("My Food Cart")

        vm.cartItems.forEachIndexed { index, item ->

            FoodItemUI(
                item = item,
                onAdd = { vm.increase(index) },
                onRemove = { vm.decrease(index) }
            )
        }

        val total = vm.cartItems.sumOf {
            it.food.price * it.quantity
        }

        Text("Total Bill: ₹$total")
    }
}

@Composable
fun FoodItemUI(
    item: CCartItem,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.food.name)
            Text("₹${item.food.price}")
        }

        Button(onClick = onRemove) {
            Text("-")
        }

        Text(
            text = "${item.quantity}",
            modifier = Modifier.padding(10.dp)
        )

        Button(onClick = onAdd) {
            Text("+")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartPreview() {
    FoodCartScreen(viewModel<FoodViewModel>())
}

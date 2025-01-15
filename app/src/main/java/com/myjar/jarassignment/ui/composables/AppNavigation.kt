// UI (AppNavigation.kt)
package com.myjar.jarassignment.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "item_list"
    ) {
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier,
    searchQuery: String,
    onSearchTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchTextChange,
        label = { Text("Search Products") },
        modifier = modifier
    )
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    navController: NavHostController
) {
    val filteredItems by viewModel.filteredItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            searchQuery = searchQuery,
            onSearchTextChange = { viewModel.updateSearchQuery(it) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredItems.isEmpty()) {
                item {
                    Text(
                        text = "No items available",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                items(filteredItems) { item ->
                    ItemCard(
                        item = item,
                        onClick = { navController.navigate("item_detail/${item.id}") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = "Name: ${item.name}", fontWeight = FontWeight.Bold, color = Color.Black)
        item.data?.let { data ->
            data.color?.let { Text(text = "Color: $it") }
            data.capacity?.let { Text(text = "Capacity: $it") }
            data.price?.let { Text(text = "Price: $it") }
            data.capacityGB?.let { Text(text = "Capacity GB: $it") }
            data.screenSize?.let { Text(text = "Screen Size: $it") }
            data.description?.let { Text(text = "Description: $it") }
            data.generation?.let { Text(text = "Generation: $it") }
            data.strapColour?.let { Text(text = "Strap Colour: $it") }
            data.caseSize?.let { Text(text = "Case Size: $it") }
            data.cpuModel?.let { Text(text = "CPU Model: $it") }
            data.hardDiskSize?.let { Text(text = "Hard Disk Size: $it") }
        }
    }
}

@Composable
fun ItemDetailScreen(itemId: String?) {
    Text(
        text = "Item Details for ID: ${itemId ?: "Unknown"}",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

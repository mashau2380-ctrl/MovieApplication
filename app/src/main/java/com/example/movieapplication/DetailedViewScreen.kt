package com.example.movieapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapplication.ui.theme.MovieApplicationTheme
import kotlin.text.clear
import kotlin.text.get

class DetailedViewScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MovieApplicationTheme {
                DetailedViewScreen(
                    onBack = {
                        // Navigate back to EnterItems screen
                        val intent = Intent(this, MainScreen::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    },
                    onClear = {
                        // Clear all added items
                        MainScreen.selectedItems.clear()
                        MainScreen.selectedCategories.clear()
                        MainScreen.selectedQuantities.clear()
                        MainScreen.selectedComments.clear()
                    }
                )
            }
        }
    }
}

@Composable
fun DetailedViewScreen(onBack: () -> Unit, onClear: () -> Unit) {
    val items = MainScreen.selectedItems
    val categories = MainScreen.selectedCategories
    val quantities = MainScreen.selectedQuantities
    val comments = MainScreen.selectedComments

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Your Packing List", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            if (items.isEmpty()) {
                // Empty-state card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No items yet", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "You haven't added any items. Go back and add from the available list.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item, style = MaterialTheme.typography.titleMedium)
                                    Text("x${quantities[index]}", style = MaterialTheme.typography.titleSmall)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text("Category: ${categories[index]}", style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(6.dp))
                                Text("Note: ${comments[index]}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onBack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back to Main Screen")
                }

                Button(
                    onClick = { onClear () },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text("Clear List")
                }
            }
        }
    }
}

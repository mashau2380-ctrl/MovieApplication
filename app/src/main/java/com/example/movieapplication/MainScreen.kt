package com.example.movieapplication


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.movieapplication.ui.theme.MovieApplicationTheme

class EnterItems : ComponentActivity() {

    companion object {
        // Available items (from picture)
        val availableItems = arrayOf("T-shirts and pants", "Toothbrush", "Shoes", "Passport")
        val availableCategories = arrayOf("Clothing", "Toiletries", "Clothing", "Documents")
        val availableQuantities = arrayOf(5, 1, 2, 1)
        val availableComments = arrayOf(
            "Comfortable for travel",
            "Essential for hygiene",
            "Walking and smart casual",
            "Don't forget this!"
        )

        // User-selected packing lists (parallel arrays)
        val selectedItems = mutableListOf<String>()
        val selectedCategories = mutableListOf<String>()
        val selectedQuantities = mutableListOf<Int>()
        val selectedComments = mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MovieApplicationTheme {
                var expanded by remember { mutableStateOf(false) }
                var chosenItem by remember { mutableStateOf("") }
                var category by remember { mutableStateOf("") }
                var quantity by remember { mutableStateOf("") }
                var comment by remember { mutableStateOf("") }

                // If user selects an available item, auto-fill category & default quantity/comment
                fun onItemSelected(index: Int) {
                    chosenItem = availableItems[index]
                    category = availableCategories[index]
                    quantity = availableQuantities[index].toString()
                    comment = availableComments[index]
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text("Enter Details for Packing List", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Available items row (informational)
                        Text("Available items (tap to auto-fill):", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(availableItems) { index, name ->
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .clickable { onItemSelected(index) }
                                ) {
                                    Text(
                                        text = name,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Dropdown/select field for item (user can still type or choose)
                        Box {
                            OutlinedTextField(
                                value = chosenItem,
                                onValueChange = { chosenItem = it },
                                label = { Text("Item name (choose from available)") },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                EnterItems.availableItems.forEachIndexed { idx, name ->
                                    DropdownMenuItem(
                                        text = { Text(name) },
                                        onClick = {
                                            expanded = false
                                            onItemSelected(idx)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Quantity") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            label = { Text("Comment (optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(
                                onClick = {
                                    // Validate item exists in available list
                                    val idx = availableItems.indexOf(chosenItem)
                                    if (idx == -1) {
                                        Toast.makeText(this@EnterItems, "Error: choose an item from the available list.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    val qty = quantity.toIntOrNull()
                                    if (qty == null || qty <= 0) {
                                        Toast.makeText(this@EnterItems, "Enter a valid quantity (greater than 0).", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    // Add to parallel arrays
                                    selectedItems.add(chosenItem)
                                    selectedCategories.add(category.ifEmpty { availableCategories[idx] })
                                    selectedQuantities.add(qty)
                                    selectedComments.add(if (comment.isEmpty()) availableComments[idx] else comment)

                                    Toast.makeText(this@EnterItems, "Added: $chosenItem", Toast.LENGTH_SHORT).show()

                                    // clear entry fields
                                    chosenItem = ""
                                    category = ""
                                    quantity = ""
                                    comment = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                            ) {
                                Text("Add")
                            }

                            Button(onClick = {
                                startActivity(Intent(this@EnterItems, DisplayItems::class.java))
                            }) {
                                Text("Display")
                            }

                            OutlinedButton(onClick = {
                                startActivity(Intent(this@EnterItems, MainActivity::class.java))
                                finish()
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Exit")
                                Spacer(Modifier.width(6.dp))
                                Text("Exit")
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Helpful hint
                        Text(
                            "Tip: tap an available item above to auto-fill fields, then press Add.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

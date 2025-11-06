package com.example.movieapplication


import android.R.attr.rating
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

class MainScreen : ComponentActivity() {

    companion object {
        // Available items (from picture)
        val availableMovie = arrayOf("THe GodFather", "The Dark Knight Pulp Fiction","Oh Schuks... I'm Gatvol",
            "Die Fighting","Hobbs and Shaw")
        val availableDirector = arrayOf("Francis Ford Coppola", "Quentin Tarantino","Leon Schuster & Willie Esterhuizen",
            "Fabien Gorchon","David Leitch")
        val availableRating = arrayOf(5, 4, 5, 3, 3.6)
        val availableComments = arrayOf(
            "A masterpiece of cinema",
            "Quirky and captivating",
            "You'll laugh like never before",
            "Fighting from the begging till end",
            "A wild ride of action and comedy"

        )

        // User-selected packing lists (parallel arrays)
        val selectedMovie = mutableListOf<String>()
        val selecteddirector = mutableListOf<String>()
        val selectedRating = mutableListOf<Int>()
        val selectedComments = mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MovieApplicationTheme {
                var expanded by remember { mutableStateOf(false) }
                var Movie by remember { mutableStateOf("") }
                var director by remember { mutableStateOf("") }
                var Rating by remember { mutableStateOf("") }
                var comment by remember { mutableStateOf("") }

                // If user selects an available item, auto-fill category & default quantity/comment
                fun onItemSelected(index: Int) {
                    Movie = availableMovie[index]
                    director = availableDirector[index]
                    Rating =availableRating[index].toString()
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
                        Text("Enter Details for Movie", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Available movie row (informational)
                        Text("Available Movie (tap to auto-fill):", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(availableMovie) { index, name ->
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

                        // Dropdown/select field for movie (user can still type or choose)
                        Box {
                            OutlinedTextField(
                                value = Movie,
                                onValueChange = { Movie = it },
                                label = { Text("Movie title (choose from available)") },
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
                                MainScreen.availableMovie.forEachIndexed { idx, name ->
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
                            value = director,
                            onValueChange = { director = it },
                            label = { Text("Director") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = Rating,
                            onValueChange = { Rating = it },
                            label = { Text("Rating") },
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
                                    // Validate movie exists in available list
                                    val idx =availableMovie.indexOf(Movie)
                                    if (idx == -1) {
                                        Toast.makeText(this@MainScreen, "Error: choose an Movie from the available list.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    val rating = Rating.toIntOrNull()
                                    if (rating == null || rating <= 0) {
                                        Toast.makeText(this@MainScreen, "Enter a valid Rating (greater than 0).", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    // Add to parallel arrays
                                    selectedMovie.add(Movie)
                                    selecteddirector.add(director.ifEmpty { availableDirector[idx] })
                                    selectedRating.add(rating)
                                    selectedComments.add(if (comment.isEmpty()) availableComments[idx] else comment)

                                    Toast.makeText(this@MainScreen, "Added: $Movie", Toast.LENGTH_SHORT).show()

                                    // clear entry fields
                                    Movie = ""
                                    director = ""
                                    Rating = ""
                                    comment = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                            ) {
                                Text("Add Movie")
                            }

                            Button(onClick = {
                                startActivity(Intent(this@MainScreen, DetailedViewScreen::class.java))
                            }) {
                                Text("Review Movie")
                            }

                            OutlinedButton(onClick = {
                                startActivity(Intent(this@MainScreen, MainActivity::class.java))
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
                            "Tip: tap an available Movie above to auto-fill fields, then press Add Movie.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

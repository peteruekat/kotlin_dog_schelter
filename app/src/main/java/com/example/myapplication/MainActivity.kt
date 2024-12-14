package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DogListApp()
                }
            }
        }
    }
}

data class Dog(
    val name: String,
    var isFavorite: Boolean = false
)

@Composable
fun CustomSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isError) Color.Red else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = if (isError) Color.Red else Color.Black
            ),
            cursorBrush = SolidColor(if (isError) Color.Red else Color.Black),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            "Poszukaj lub dodaj pieska 🐕",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun DogListApp() {
    var searchText by remember { mutableStateOf("") }
    var dogs by remember { mutableStateOf(listOf<Dog>()) }
    var isError by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }

    val dogNames = remember { mutableSetOf<String>() }
    val purpleGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF9C27B0),
            Color(0xFFE1BEE7)
        )
    )

    val filteredDogs = remember(dogs, searchText, isSearching) {
        if (isSearching && searchText.isNotEmpty()) {
            dogs.filter { it.name.contains(searchText, ignoreCase = true) }
        } else {
            dogs
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomSearchField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    isError = false
                    isSearching = false
                },
                isError = isError,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { isSearching = true },
                enabled = searchText.isNotEmpty(),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Szukaj",
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(
                onClick = {
                    if (!dogNames.contains(searchText)) {
                        dogs = dogs + Dog(searchText)
                        dogNames.add(searchText)
                        searchText = ""
                        isError = false
                        isSearching = false
                    } else {
                        isError = true
                    }
                },
                enabled = searchText.isNotEmpty(),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj",
                    tint = Color(0xFF673AB7),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (isError) {
            Text(
                text = "Piesek o tej nazwie już istnieje!",
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text("🐕: ${dogs.size}", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text("💜: ${dogs.count { it.isFavorite }}", fontSize = 16.sp)
        }

        LazyColumn {
            items(filteredDogs.filter { it.isFavorite }.sortedBy { it.name }) { dog ->
                DogItem(
                    dog = dog,
                    gradient = purpleGradient,
                    onFavoriteClick = {
                        dogs = dogs.map {
                            if (it.name == dog.name) it.copy(isFavorite = !it.isFavorite)
                            else it
                        }
                    },
                    onDeleteClick = {
                        dogs = dogs.filter { it.name != dog.name }
                        dogNames.remove(dog.name)
                    }
                )
            }

            items(filteredDogs.filter { !it.isFavorite }.sortedBy { it.name }) { dog ->
                DogItem(
                    dog = dog,
                    gradient = purpleGradient,
                    onFavoriteClick = {
                        dogs = dogs.map {
                            if (it.name == dog.name) it.copy(isFavorite = !it.isFavorite)
                            else it
                        }
                    },
                    onDeleteClick = {
                        dogs = dogs.filter { it.name != dog.name }
                        dogNames.remove(dog.name)
                    }
                )
            }
        }
    }
}

@Composable
fun DogItem(
    dog: Dog,
    gradient: Brush,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(gradient)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🐕", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(dog.name, fontSize = 16.sp)
            }

            Row {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (dog.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Ulubione",
                        modifier = Modifier.size(24.dp),
                        tint = if (dog.isFavorite) {
                            Color(0xFF9C27B0)
                        } else {
                            Color.Gray
                        }
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Usuń",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
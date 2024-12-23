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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.util.UUID
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

data class Dog(
    val id: String = UUID.randomUUID().toString(), // Add unique identifier
    val name: String,
    var breed: String = "",
    var imageUrl: String = "",
    var isFavorite: Boolean = false
)

data class User(
    var name: String = "",
    var imageUrl: String = ""
)

sealed class Screen(val route: String) {
    object DogList : Screen("dogList")
    object DogDetail : Screen("dogDetail/{dogId}") {
        fun createRoute(dogId: String) = "dogDetail/$dogId"
    }
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, "Settings")
            }
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Default.Person, "Profile")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun MainApp() {
    var user by remember { mutableStateOf(User()) }
    var dogs by remember { mutableStateOf(listOf<Dog>()) }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.DogList.route) {
        composable(Screen.DogList.route) {
            DogListScreen(
                dogs = dogs,
                onDogClick = { dog ->
                    navController.navigate(Screen.DogDetail.createRoute(dog.id))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onDogsUpdate = { newDogs ->
                    dogs = newDogs
                }
            )
        }

        composable(
            route = Screen.DogDetail.route,
            arguments = listOf(navArgument("dogId") { type = NavType.StringType })
        ) { backStackEntry ->
            val dogId = backStackEntry.arguments?.getString("dogId")
            val dog = dogs.find { it.id == dogId }
            if (dog != null) {
                DogDetailScreen(
                    dog = dog,
                    onNavigateBack = { navController.navigateUp() },
                    onDogUpdate = { updatedDog ->
                        dogs = dogs.map { if (it.id == updatedDog.id) updatedDog else it }
                    }
                )
            }
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                user = user,
                onNavigateBack = { navController.navigateUp() },
                onUserUpdate = { updatedUser ->
                    user = updatedUser
                }
            )
        }
    }
}

@Composable
fun DogListScreen(
    dogs: List<Dog>,
    onDogClick: (Dog) -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onDogsUpdate: (List<Dog>) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Doggos",
            onSettingsClick = onSettingsClick,
            onProfileClick = onProfileClick
        )
        DogListApp(
            dogs = dogs,
            onDogClick = onDogClick,
            onDogsUpdate = onDogsUpdate
        )
    }
}

@Composable
fun DogDetailScreen(
    dog: Dog,
    onNavigateBack: () -> Unit,
    onDogUpdate: (Dog) -> Unit
) {
    var editedDog by remember { mutableStateOf(dog) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Detale",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("🐕",
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 48.sp
                )
            }

            OutlinedTextField(
                value = editedDog.name,
                onValueChange = {
                    editedDog = editedDog.copy(name = it)
                    onDogUpdate(editedDog)
                },
                label = { Text("Imię") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editedDog.breed,
                onValueChange = {
                    editedDog = editedDog.copy(breed = it)
                    onDogUpdate(editedDog)
                },
                label = { Text("Rasa") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Ustawienia",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )
        // Add settings content here
    }
}

@Composable
fun ProfileScreen(
    user: User,
    onNavigateBack: () -> Unit,
    onUserUpdate: (User) -> Unit
) {
    var editedUser by remember { mutableStateOf(user) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = "Profil",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }

            OutlinedTextField(
                value = editedUser.name,
                onValueChange = {
                    editedUser = editedUser.copy(name = it)
                    onUserUpdate(editedUser)
                },
                label = { Text("Nazwa użytkownika") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

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
fun DogListApp(
    dogs: List<Dog>,
    onDogClick: (Dog) -> Unit,
    onDogsUpdate: (List<Dog>) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }

    val dogNames = remember { mutableSetOf<String>() }
    dogNames.clear()
    dogNames.addAll(dogs.map { it.name })

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
                        onDogsUpdate(dogs + Dog(name = searchText))
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
                        onDogsUpdate(dogs.map {
                            if (it.id == dog.id) it.copy(isFavorite = !it.isFavorite)
                            else it
                        })
                    },
                    onDeleteClick = {
                        onDogsUpdate(dogs.filter { it.id != dog.id })
                        dogNames.remove(dog.name)
                    },
                    onClick = { onDogClick(dog) }
                )
            }

            items(filteredDogs.filter { !it.isFavorite }.sortedBy { it.name }) { dog ->
                DogItem(
                    dog = dog,
                    gradient = purpleGradient,
                    onFavoriteClick = {
                        onDogsUpdate(dogs.map {
                            if (it.id == dog.id) it.copy(isFavorite = !it.isFavorite)
                            else it
                        })
                    },
                    onDeleteClick = {
                        onDogsUpdate(dogs.filter { it.id != dog.id })
                        dogNames.remove(dog.name)
                    },
                    onClick = { onDogClick(dog) }
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
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
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
                Column {
                    Text(dog.name, fontSize = 16.sp)
                    if (dog.breed.isNotEmpty()) {
                        Text(
                            dog.breed,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
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
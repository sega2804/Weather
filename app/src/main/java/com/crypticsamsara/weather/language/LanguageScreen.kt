package com.crypticsamsara.weather.language

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crypticsamsara.weather.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // Get languages from ViewModel - use stateIn for better initialization
    val languages by viewModel.supportedLanguages.collectAsState(initial = emptyList())
    // Safe initialization
    var selectedLanguage by remember {
        mutableStateOf<AuthViewModel.NigerianLanguage?>(null)
    }
   // val isMobile = LocalConfiguration.current.screenWidthDp < 600

    // Set initial selection after languages load
    LaunchedEffect(languages) {
        if (languages.isNotEmpty() && selectedLanguage == null) {
            selectedLanguage = languages.first()
        }
    }

    // Show loading if languages not ready
    if (languages.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
/*
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFE8F5E9), Color(0xFFFFF8E1)),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {


        // Animated Blobs background
    AnimatedBlobs()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

 */

    Scaffold(
    // Top App Bar
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = "App Logo",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "AgriCast",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0D9488), Color(0xFF059669))
                        )
                    )
            )
        },
         containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFE8F5E9), Color(0xFFFFF8E1)),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        ) {
        AnimatedBlobs()



        // Main Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

           // Welcome Section
            WelcomeSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Language cards grid
            LanguageGrid(languages = languages,
                selectedLanguage = selectedLanguage,
                onLanguageSelected =
             { language ->
                selectedLanguage = language
                viewModel.setLanguage(language.code)
                navController.navigate("login") {
                    popUpTo("language") { inclusive = true }
                }
            }
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Features Section
            FeaturesSection()

            Spacer(modifier = Modifier.height(16.dp))


        // Footer note
            Text(
                text = "Your language preference will be saved and can be changed later in settings",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
    }
}


@Composable
private fun WelcomeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF2196F3), Color(0xFF4CAF50))
                    ),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = "Cloud Icon",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to AgriCast",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Choose your preferred language to get started with personalized weather and farming guidance",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}


@Composable
private fun LanguageGrid(
    languages: List<AuthViewModel.NigerianLanguage>,
    selectedLanguage: AuthViewModel.NigerianLanguage?,
    onLanguageSelected: (AuthViewModel.NigerianLanguage) -> Unit
) {
    val columns = when (LocalConfiguration.current.screenWidthDp) {
        in 0..600 -> 1
        in 601..900 -> 2
        else -> 3
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
            .height((languages.size / columns + 1) * 200.dp)
    ) {
        items(languages) { language ->
            LanguageCard(
                lang = language,
                isSelected = language == selectedLanguage,
                onSelect = { onLanguageSelected(language) }
            )
        }
    }
}


@Composable
fun LanguageCard(
    lang: AuthViewModel.NigerianLanguage,
    isSelected: Boolean,
    onSelect: () -> Unit
) {

    Card(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF2196F3) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF2196F3), Color(0xFF4CAF50))
                        ),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = lang.flagEmoji,
                    fontSize = 24.sp,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = lang.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lang.nativeName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lang.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select Language",
                tint = if (isSelected) Color(0xFF2196F3) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



@Composable
private fun FeaturesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Text(
            text = "What you'll get with AgriCast",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        FeatureItem(
            icon = Icons.Default.Cloud,
            title = "Weather Forecast",
            description = "7-day weather predictions for your farm",
            iconColor = Color(0xFF2196F3)
        )

        FeatureItem(
            icon = Icons.Default.Spa,
            title = "Farming Advice",
            description = "Personalized crop recommendations",
            iconColor = Color(0xFF4CAF50)
        )

        FeatureItem(
            icon = Icons.Default.PhoneAndroid,
            title = "Easy Access",
            description = "Works on any device",
            iconColor = Color(0xFF9C27B0)
        )
    }
}


@Composable
fun FeatureItem(icon: ImageVector,
                title: String,
                description: String,
                iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .background(color = iconColor.copy(alpha = 0.2f), shape = CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun AnimatedBlobs() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Blob 1 (Blue)
        AnimatedBlob(
            color = Color(0xFF90CAF9).copy(alpha = 0.2f),
            size = 300.dp,
            startX = -0.2f,
            startY = -0.1f,
            duration = 15000
        )

        // Blob 2 (Green)
        AnimatedBlob(
            color = Color(0xFFA5D6A7).copy(alpha = 0.2f),
            size = 250.dp,
            startX = 0.8f,
            startY = 0.7f,
            duration = 18000
        )

        // Blob 3 (Yellow)
        AnimatedBlob(
            color = Color(0xFFFFF59D).copy(alpha = 0.2f),
            size = 280.dp,
            startX = 0.3f,
            startY = 0.8f,
            duration = 20000
        )
    }
}

@Composable
private fun AnimatedBlob(
    color: Color,
    size: Dp,
    startX: Float,
    startY: Float,
    duration: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "blob_animation")

    val offsetX by infiniteTransition.animateFloat(
        initialValue = startX,
        targetValue = startX + 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob_offset_x"
    )
    val offsetY by infiniteTransition.animateFloat(
        initialValue = startY,
        targetValue = startY + 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob_offset_y"
    )

    Box(
        modifier = Modifier
            .offset(
                x = (offsetX * LocalConfiguration.current.screenWidthDp).dp,
                y = (offsetY * LocalConfiguration.current.screenHeightDp).dp
            )
            .size(size)
            .background(color, shape = CircleShape)
            .blur(30.dp)
    )
}

























/*
            items(languages) { language ->
                LanguageCard(
                    lang = language,
                    isSelected = language == selectedLanguage,
                    onSelect = {
                        selectedLanguage = language
                        viewModel.setLanguage(language.code)
                        navController.navigate("login") {
                            popUpTo("language") { inclusive = true }
                        }
                    }
                )
            }
        }



 */
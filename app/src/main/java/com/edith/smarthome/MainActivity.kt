
package com.edith.smarthome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalContext

private val BG = Color(0xFF070B16)
private val GLASS = Color(0xFF18253A)
private val CYAN = Color(0xFF45D9FF)
private val MUTED = Color(0xFF9BAFC8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EdithApp() }
    }
}

@Composable
fun EdithApp() {
    var page by remember { mutableStateOf("Home") }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = CYAN,
            background = BG,
            surface = GLASS
        )
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF14243B),
                            BG,
                            Color(0xFF090D1A)
                        )
                    )
                )
        ) {
            Row(
                Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "E.D.I.T.H.",
                        color = CYAN,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "PERSONAL HOME SYSTEM",
                        color = MUTED,
                        fontSize = 10.sp
                    )
                }
                Text("● ONLINE", color = Color(0xFF54E6A5))
            }

            Box(Modifier.weight(1f)) {
                when (page) {
                    "Home" -> HomeScreen()
                    "Pixel Studio" -> PixelScreen()
                    else -> SettingsScreen()
                }
            }

            NavigationBar(
                containerColor = Color(0xEE101A2B)
            ) {
                listOf("Home", "Pixel Studio", "Settings").forEach { item ->
                    NavigationBarItem(
                        selected = page == item,
                        onClick = { page = item },
                        icon = {
                            Text(
                                when (item) {
                                    "Home" -> "⌂"
                                    "Pixel Studio" -> "✦"
                                    else -> "⚙"
                                },
                                fontSize = 21.sp
                            )
                        },
                        label = { Text(item, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CYAN,
                            selectedTextColor = CYAN,
                            unselectedTextColor = MUTED,
                            indicatorColor = Color(0xFF263E58)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = GLASS.copy(alpha = 0.78f)
        ),
        border = BorderStroke(
            1.dp,
            Color.White.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String = "") {
    Text(
        title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    if (subtitle.isNotEmpty()) {
        Text(subtitle, color = MUTED, fontSize = 11.sp)
    }
}

@Composable
fun HomeScreen() {
    val relays = remember {
        mutableStateListOf(false, false, false, false)
    }

    Column(
        Modifier.verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GlassCard {
            Text("WELCOME BACK, SIR", color = MUTED, fontSize = 12.sp)
            Text(
                "EDITH is ready.",
                color = CYAN,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Your smart home at a glance.", color = Color.White)
        }

        GlassCard {
            SectionTitle("System Status", "Device overview")
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusItem("ESP32", "Connected?")
                StatusItem("LEDs", "Ready")
                StatusItem("Relays", "4 channels")
            }
            Text(
                "Connection status is a display placeholder.",
                color = MUTED,
                fontSize = 10.sp
            )
        }

        GlassCard {
            SectionTitle("Relay Control", "Four channel switches")
            relays.forEachIndexed { index, state ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Relay ${index + 1}", color = Color.White)
                        Text(
                            if (state) "ON" else "OFF",
                            color = if (state) CYAN else MUTED,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = state,
                        onCheckedChange = { relays[index] = it }
                    )
                }
            }
        }

        GlassCard {
            SectionTitle("Pixel Studio", "50 addressable LEDs")
            Text(
                "Control your RGB lighting and effects.",
                color = MUTED
            )
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun StatusItem(title: String, value: String) {
    Column {
        Text(title, color = MUTED, fontSize = 11.sp)
        Text(value, color = CYAN, fontSize = 13.sp)
    }
}

@Composable
fun PixelScreen() {
    val colors = listOf(
        Color.Red,
        Color(0xFFFF7900),
        Color.Yellow,
        Color.Green,
        Color.Cyan,
        Color.Blue,
        Color.Magenta,
        Color.White,
        Color(0xFF00FF9D),
        Color(0xFF9B59FF)
    )

    val effects = remember {
        mutableStateListOf(
            "Solid Color", "Rainbow", "Rainbow Cycle",
            "Breathing", "Color Wipe", "Running Lights",
            "Chase", "Theater Chase", "Twinkle",
            "Fire", "Fireworks", "Meteor",
            "Scanner", "Comet", "Sparkle",
            "Confetti", "Gradient", "Wave",
            "Police Lights", "Music Reactive"
        )
    }

    val favorites = remember { mutableStateListOf<String>() }

    var selectedColor by remember { mutableStateOf(CYAN) }
    var selectedEffect by remember { mutableStateOf("Rainbow") }
    var brightness by remember { mutableFloatStateOf(180f) }
    var speed by remember { mutableFloatStateOf(50f) }
    var intensity by remember { mutableFloatStateOf(75f) }
    var ledPower by remember { mutableStateOf(true) }
    var reverse by remember { mutableStateOf(false) }
    var favoriteOnly by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Ready to configure LEDs") }

    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        GlassCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    SectionTitle("PIXEL STUDIO")
                    Text("50 LED RGB Controller", color = MUTED, fontSize = 11.sp)
                }
                Switch(
                    checked = ledPower,
                    onCheckedChange = { ledPower = it }
                )
            }

            Spacer(Modifier.height(14.dp))

            Box(
                Modifier.fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(selectedColor, Color(0xFF111A30))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (ledPower) "LED POWER ON" else "LED POWER OFF",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                message,
                color = MUTED,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        GlassCard {
            SectionTitle("RGB Color", "Choose your LED color")
            Spacer(Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(116.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(colors) { color ->
                    Box(
                        Modifier.size(42.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                if (selectedColor == color) 3.dp else 1.dp,
                                Color.White,
                                CircleShape
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }

            Text(
                "Selected color",
                color = MUTED,
                fontSize = 11.sp
            )
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(35.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "RGB color selected",
                    color = Color.White
                )
            }
        }

        GlassCard {
            SectionTitle("LED Adjustments", "Fine tune your lighting")

            ControlSlider("Brightness", brightness, 255f) {
                brightness = it
            }
            ControlSlider("Animation Speed", speed, 100f) {
                speed = it
            }
            ControlSlider("Effect Intensity", intensity, 100f) {
                intensity = it
            }
        }

        GlassCard {
            SectionTitle("Effects Library", "Select an animation")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = !favoriteOnly,
                    onClick = { favoriteOnly = false },
                    label = { Text("All effects") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = favoriteOnly,
                    onClick = { favoriteOnly = true },
                    label = { Text("♥ Favorites") }
                )
            }

            val visibleEffects = effects.filter {
                !favoriteOnly || it in favorites
            }

            visibleEffects.forEach { effect ->
                Row(
                    Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (selectedEffect == effect)
                                Color(0xFF25435B)
                            else Color(0xFF111C2D)
                        )
                        .clickable {
                            selectedEffect = effect
                            message = "$effect selected"
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        effect,
                        color = if (selectedEffect == effect) CYAN
                        else Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        if (effect in favorites) "♥" else "♡",
                        color = Color(0xFFFF78A8),
                        fontSize = 22.sp,
                        modifier = Modifier.clickable {
                            if (effect in favorites)
                                favorites.remove(effect)
                            else favorites.add(effect)
                        }
                    )
                }
            }
        }

        GlassCard {
            SectionTitle("Animation Direction")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { reverse = false }) {
                    Text("Forward")
                }
                OutlinedButton(onClick = { reverse = true }) {
                    Text("Reverse")
                }
            }
            Text(
                if (reverse) "Reverse direction selected"
                else "Forward direction selected",
                color = MUTED,
                fontSize = 11.sp
            )
        }

        GlassCard {
            SectionTitle("Custom Effect Creator")
            OutlinedTextField(
                value = customName,
                onValueChange = { customName = it },
                label = { Text("New effect name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (customName.isNotBlank() &&
                        customName !in effects
                    ) {
                        effects.add(customName.trim())
                        selectedEffect = customName.trim()
                        message = "Custom effect added"
                        customName = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("＋ Add Custom Effect")
            }
        }

        GlassCard {
            SectionTitle("Save Configuration")
            Button(
                onClick = {
                    message = "Preset configuration selected: $selectedEffect"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Current Preset")
            }
            OutlinedButton(
                onClick = {
                    brightness = 180f
                    speed = 50f
                    intensity = 75f
                    selectedColor = CYAN
                    selectedEffect = "Rainbow"
                    reverse = false
                    message = "Default settings restored"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset Controls")
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun ControlSlider(
    title: String,
    value: Float,
    max: Float,
    onChange: (Float) -> Unit
) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White)
            Text(value.toInt().toString(), color = CYAN)
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = 0f..max
        )
    }
}

@Composable
fun SettingsScreen() {
    var ip by remember { mutableStateOf("192.168.4.1") }
    var deviceName by remember { mutableStateOf("EDITH") }

    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GlassCard {
            SectionTitle("Settings", "Personalize your EDITH system")

            OutlinedTextField(
                value = deviceName,
                onValueChange = { deviceName = it },
                label = { Text("Assistant name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = ip,
                onValueChange = { ip = it },
                label = { Text("ESP32 IP address") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        GlassCard {
            SectionTitle("System Information")
            Text("Device: ESP32-C3 Super Mini", color = Color.White)
            Text("LED strip: WS2812B", color = Color.White)
            Text("LED count: 50", color = Color.White)
            Text("App: EDITH Smart Home", color = Color.White)
        }

        GlassCard {
            SectionTitle("Assistant")
            Text("Wake word: Edith", color = Color.White)
            Text("AI service: Gemini (integration pending)", color = MUTED)
        }
    }
}

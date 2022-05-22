package de.enricoe.minigames.gui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.SortAlphaDown
import compose.icons.fontawesomeicons.solid.User
import de.enricoe.minigames.game.Game
import de.enricoe.minigames.game.GameProperties
import de.enricoe.minigames.game.allGames
import de.enricoe.minigames.gui.Application.blurBackground
import de.enricoe.minigames.gui.utils.colors.ContentColor
import de.enricoe.minigames.gui.utils.colors.DefaultTextColor
import de.enricoe.minigames.gui.utils.colors.colorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.full.createInstance

var openGame by mutableStateOf<Game?>(null)

@Composable
fun HomeScreen() = Box(Modifier.fillMaxSize(), Alignment.Center) {
    var searchTerm by remember { mutableStateOf("") }
    var currentOrder by remember { mutableStateOf(GameOrder.ALPHABET) }

    Column(Modifier.fillMaxSize()) {
        /*
        * Search
        * */
        // Searchbar
        Row(
            Modifier.fillMaxWidth().padding(top = 6.dp, start = 24.dp, end = 24.dp).height(40.dp)
                .background(colorScheme().surfaceVariant, RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.Search, "Search", Modifier.padding(vertical = 8.dp))
            Box(contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    searchTerm,
                    onValueChange = { searchTerm = it },
                    modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 8.dp).padding(vertical = 4.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = ContentColor()
                    ),
                    singleLine = true
                )
                if (searchTerm.isEmpty()) {
                    Text("Search...", Modifier.padding(start = 4.dp), fontSize = 18.sp)
                }
            }
        }

        // Orders
        Row(
            Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp, start = 24.dp, end = 24.dp).height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GameOrder.values().forEach { order ->
                val buttonColors = ButtonDefaults.elevatedButtonColors(
                    containerColor = ButtonDefaults.elevatedButtonColors().containerColor(currentOrder == order).value,
                    contentColor = ButtonDefaults.elevatedButtonColors().contentColor(currentOrder == order).value
                )

                ElevatedButton(onClick = { currentOrder = order }, colors = buttonColors) {
                    Icon(order.icon, order.displayName, Modifier.size(24.dp).padding(end = 6.dp))
                    Text(order.displayName, color = DefaultTextColor())
                }
            }
        }

        /*
        * Games
        * */
        LazyColumn {
            val range = if (currentOrder == GameOrder.ALPHABET) 'A'..'Z' else '1'..'4'
            items(range.toList()) { filter ->
                Text("$filter", Modifier.padding(start = 12.dp), fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)

                val scrollState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                LazyRow(
                    state = scrollState,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                coroutineScope.launch {
                                    scrollState.scrollBy(-delta)
                                }
                            },
                        ),
                    contentPadding = PaddingValues(end = 12.dp, bottom = 8.dp),
                    userScrollEnabled = false,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        allGames
                            .filter { properties ->
                                if (filter.isLetter()) properties.name.uppercase().first() == filter
                                else properties.maxPlayers == filter.digitToInt()
                            }.filter { properties ->
                                properties.name.contains(searchTerm, true)
                            }.sortedBy { properties ->
                                properties.name.lowercase()
                            }
                    ) { properties ->
                        GameCard(properties)
                    }
                }
            }
        }
    }
}

private enum class GameOrder(val icon: ImageVector, val displayName: String) {
    ALPHABET(FontAwesomeIcons.Solid.SortAlphaDown, "Alphabet"),
    PLAYERS(FontAwesomeIcons.Solid.User, "Players"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameCard(properties: GameProperties) {
    ElevatedButton(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                blurBackground = true
                delay(250)
                properties.kclass.createInstance().apply { start() }
            }
        },
        modifier = Modifier.width(272.dp).height(448.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
            Column {
                OutlinedCard(Modifier.fillMaxWidth().height(150.dp).padding(6.dp), RoundedCornerShape(8.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Icon(properties.icon, "Icon!")
                    }
                }

                Box(Modifier.padding(start = 6.dp, top = 4.dp)) {
                    Column {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(properties.name, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            Text("${properties.maxPlayers} Players", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                        Text(properties.description, color = DefaultTextColor(), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveGameCard(state: WindowState) {
    AnimatedVisibility(
        visible = openGame != null,
        enter = slideInVertically(initialOffsetY = { maxHeight -> maxHeight }),
        exit = slideOutVertically(targetOffsetY = { maxHeight -> maxHeight }),
    ) {
        ElevatedCard(
            Modifier
                .width(state.size.width / 6 * 5)
                .height(state.size.height / 6 * 5)
                .clip(RoundedCornerShape(12.dp))
        ) {
            openGame?.let { game ->
                Box(Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.TopEnd) {
                    FloatingActionButton({
                        CoroutineScope(Dispatchers.IO).launch {
                            openGame?.isRunning = false
                            openGame = null
                            delay(250)
                            blurBackground = false
                        }
                    }) {
                        Icon(Icons.Default.Close, "Close", Modifier.size(32.dp))
                    }

                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        Box(
                            Modifier.fillMaxSize().padding(12.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Divider(Modifier.width(128.dp).padding(top = 1.dp))
                                Text(
                                    game.properties.name,
                                    color = colorScheme().primary,
                                    fontSize = 27.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Divider(Modifier.width(192.dp).padding(top = 2.dp, bottom = 3.dp))
                                game.drawHeader()
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column {
                                        game.drawGame()
                                        game.drawFooter()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
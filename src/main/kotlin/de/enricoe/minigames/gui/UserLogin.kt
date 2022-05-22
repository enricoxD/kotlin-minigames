package de.enricoe.minigames.gui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Check
import compose.icons.fontawesomeicons.solid.CheckCircle
import compose.icons.fontawesomeicons.solid.User
import de.enricoe.minigames.game.player.HumanPlayer
import de.enricoe.minigames.gui.UserLogin.isOpen
import de.enricoe.minigames.gui.utils.colors.ContentColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object UserLogin {
    var isOpen by mutableStateOf(true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginCard(state: WindowState) {
    var userName by remember { mutableStateOf("") }
    var successfulLoginBlur by remember { mutableStateOf(false) }
    var successfulLoginIcon by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isOpen,
        exit = slideOutVertically(tween(650), targetOffsetY = { maxHeight -> (maxHeight * 1.3).toInt() }),
    ) {
        Box(contentAlignment = Alignment.Center) {
            ElevatedCard {
                Box(
                    Modifier
                        .width(state.size.width / 6 * 3)
                        .height(state.size.height / 6 * 3),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(96.dp))
                        Icon(FontAwesomeIcons.Solid.User, "User", Modifier.size(96.dp))
                        Text("Player ${Application.players.size + 1}")
                        TextField(
                            userName,
                            onValueChange = { userName = it },
                            modifier = Modifier.padding(top = 12.dp).width(384.dp),
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = ContentColor()
                            ),
                            placeholder = { "Username" },
                            singleLine = true
                        )
                        Spacer(Modifier.weight(1f))
                        Row {
                            Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (Application.players.isEmpty()) return@launch
                                    isOpen = false
                                    delay(650)
                                    Application.blurBackground = false
                                }
                            }, Modifier.padding(end = 32.dp)) {
                                Text("Confirm")
                            }

                            Button(onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (userName.isEmpty()) return@launch
                                    successfulLoginBlur = true
                                    delay(300)
                                    successfulLoginIcon = true
                                    Application.players.add(HumanPlayer(userName))
                                    userName = ""
                                    delay(1000)
                                    successfulLoginIcon = false
                                    delay(500)
                                    successfulLoginBlur = false
                                }
                            }, Modifier.padding(end = 32.dp)) {
                                Text("Add")
                            }
                        }
                    }
                }
            }


            AnimatedVisibility(
                visible = successfulLoginBlur,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200))
            ) {
                val rotation by animateFloatAsState(if (successfulLoginIcon) 360f else 0f, tween(1100))

                Box(
                    Modifier.fillMaxSize().background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedVisibility(
                        visible = successfulLoginIcon,
                        enter = slideInVertically(tween(400)) { it * 2 },
                        exit = slideOutVertically(tween(650)) { it * 2 }
                    ) {
                        Icon(
                            FontAwesomeIcons.Solid.CheckCircle,
                            "Login Successful",
                            Modifier.rotate(rotation),
                            Color.Green
                        )
                    }
                }
            }
        }
    }
}
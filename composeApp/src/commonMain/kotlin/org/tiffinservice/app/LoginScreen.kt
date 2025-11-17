package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.tiffinservice.app.ui.DarkBackground
import org.tiffinservice.app.ui.LightBackground
import org.tiffinservice.app.ui.OrangeMain

object LoginScreen : Screen {

    @Composable
    override fun Content() {
//        val navigator = LocalNavigator.current
        var phoneNumber by remember { mutableStateOf("") }

        Box(
            modifier = Modifier.fillMaxSize().background(DarkBackground)
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(60.dp))

                Row (verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("TiffinCo", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.weight(1f))

                Card (
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBackground)
                ) {
                    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Fresh, Homemade\nMeals, Delivered.",
                            fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Log in or sign up to start your delicious journey.",
                            fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(32.dp))
                        Text("Enter your mobile number", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            placeholder = { Text("e.g., 9876543210") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = OrangeMain,
                                unfocusedIndicatorColor = Color.LightGray,
                                cursorColor = OrangeMain
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
//                                navigator?.push(HomeScreen())
                                      },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                        ) {
                            Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = {
//                                navigator?.push(HomeScreen())
                                      },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Login, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Continue with Google", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
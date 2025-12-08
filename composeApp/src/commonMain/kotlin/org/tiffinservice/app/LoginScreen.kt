package org.tiffinservice.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun CustomInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val BgLight = Color(0xFFFEF9F3)
    val TextGray = Color(0xFF8A8A8A)
    val Border = Color(0xFFE5E7EB)
    val Primary = Color(0xFFFF9F1C)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextGray) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                tint = TextGray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Primary,
            unfocusedIndicatorColor = Border,
            focusedContainerColor = BgLight,
            unfocusedContainerColor = BgLight,
            cursorColor = Primary,
            focusedLeadingIconColor = Primary
        )
    )
}


@Composable
fun LoginScreen(
    onContinue: (String, String, String) -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    // Colors
    val BgLight = Color(0xFFFEF9F3)
    val Primary = Color(0xFFFF9F1C)
    val TextLight = Color(0xFF333333)
    val TextGray = Color(0xFF8A8A8A)

    // Enable button only when phone has 10 digits
    val isValid = phone.length == 10

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // 🏞 Background image with dark gradient overlay
        KamelImage(
            resource = asyncPainterResource(
                "https://lh3.googleusercontent.com/aida-public/AB6AXuDWCakqwafRTqUxWc1goqE8u8A8Guz5jTp0sbO9HerzXH_eTrgHDTJrphVtkC5pfcIA1kSIA4Nv36GmTBDTlEx93HZZ_ocxUlIEg8uNOm-BbB4EZ8youiqFzpMjzJJzb7cuLB0I_CEci2EGaRGjgbbUjVtW6eaGl20-sJ6p4mE1ApYXWgZtZVUpiFEeUku2mMCEnprXAoJPSItcG3fWZX3oDUtW3bjYNRdYI4wToId4bJIibH_cWfB5viNxtX2jFHeKPE7MfWp8dPg"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                )
        )

        // 🍱 Brand top center
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "TiffinCo",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // WHITE SHEET
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = BgLight,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 📝 Title
            Text(
                "Fresh, Homemade Meals, Delivered.",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // 📄 Subtitle
            Text(
                "Log in or sign up to start your delicious journey.",
                fontSize = 15.sp,
                color = TextGray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // 🔢 Mobile (only digits, max 10)
            CustomInput(
                value = phone,
                onValueChange = { new ->
                    if (new.length <= 10 && new.all { it.isDigit() }) {
                        phone = new
                    }
                },
                placeholder = "Mobile number",
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Number
            )

            Spacer(Modifier.height(8.dp))

            // 📧 Email
            CustomInput(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                icon = Icons.Default.Email
            )

            Spacer(Modifier.height(8.dp))

            // 👤 Username
            CustomInput(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username",
                icon = Icons.Default.Person
            )

            Spacer(Modifier.height(16.dp))

            // 🟠 Continue button
            Button(
                onClick = { onContinue(phone, email, username) },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isValid) Primary else Primary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "Continue",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            // 🧾 Terms text
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = buildAnnotatedString {
                    append("By continuing, you agree to our ")

                    withStyle(
                        SpanStyle(
                            color = Primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) { append("Terms of Service") }

                    append(" and ")

                    withStyle(
                        SpanStyle(
                            color = Primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) { append("Privacy Policy") }

                    append(".")
                },
                color = TextGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}


//@Composable
//fun LoginScreen(onContinue: (String) -> Unit){
//
//    //        val navigator = LocalNavigator.current
//    var phoneNumber by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier.fillMaxSize().background(DarkBackground)
//    ) {
//        Column (horizontalAlignment = Alignment.CenterHorizontally) {
//            Spacer(Modifier.height(60.dp))
//
//            Row (verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Default.Restaurant,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.size(40.dp)
//                )
//                Spacer(Modifier.width(12.dp))
//                Text("TiffinCo", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Card (
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
//                colors = CardDefaults.cardColors(containerColor = LightBackground)
//            ) {
//                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        "Fresh, Homemade\nMeals, Delivered.",
//                        fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
//                    )
//
//                    Spacer(Modifier.height(8.dp))
//                    Text(
//                        "Log in or sign up to start your delicious journey.",
//                        fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center
//                    )
//
//                    Spacer(Modifier.height(32.dp))
//                    Text("Enter your mobile number", fontSize = 16.sp, fontWeight = FontWeight.Medium)
//                    Spacer(Modifier.height(8.dp))
//
//                    OutlinedTextField(
//                        value = phoneNumber,
//                        onValueChange = { phoneNumber = it },
//                        placeholder = { Text("e.g., 9876543210") },
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = TextFieldDefaults.colors(
//                            focusedContainerColor = Color.White,
//                            unfocusedContainerColor = Color.White,
//                            focusedIndicatorColor = OrangeMain,
//                            unfocusedIndicatorColor = Color.LightGray,
//                            cursorColor = OrangeMain
//                        )
//                    )
//
//                    Spacer(Modifier.height(16.dp))
//
//                    Button(
//                        onClick = {
////                                navigator?.push(HomeScreen())
//                            onContinue(phoneNumber)
//                        },
//                        modifier = Modifier.fillMaxWidth().height(56.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
//                    ) {
//                        Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
//                    }
//
//                    Spacer(Modifier.height(16.dp))
//
//                    OutlinedButton(
//                        onClick = {
////                                navigator?.push(HomeScreen())
//                        },
//                        modifier = Modifier.fillMaxWidth().height(56.dp),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(Icons.Default.Login, null)
//                        Spacer(Modifier.width(8.dp))
//                        Text("Continue with Google", color = Color.Black)
//                    }
//                }
//            }
//        }
//    }
//}
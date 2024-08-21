package com.example.fontendpicshare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(16.dp)
    ) {
        // Nút "Back" ở góc trên bên trái
        Button(
            onClick = { navController.popBackStack() },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(text = "Back", color = Color.Black)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "REGISTER",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Button nhập Tên
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên của bạn") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFA726)
                )
            )

            // Button nhập Tài khoản
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Tài khoản") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFA726)
                )
            )

            // Button nhập Mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFA726)
                )
            )

            // Button xác nhận Mật khẩu
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Xác nhận mật khẩu") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFA726)
                )
            )

            // Hiển thị lỗi nếu có
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Nút Đăng ký
            Button(
                onClick = {
                    scope.launch {
                        if (password != confirmPassword) {
                            errorMessage = "Mật khẩu và xác nhận mật khẩu không khớp"
                        } else if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            errorMessage = "Vui lòng điền đầy đủ thông tin"
                        } else {
                            errorMessage = ""
                            // Giả sử đăng ký thành công, điều hướng tới trang login
                            navController.navigate("loginForm") {
                                popUpTo("registerForm") { inclusive = true }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(text = "Đăng ký", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}

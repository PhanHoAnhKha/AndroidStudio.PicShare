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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }  // Trạng thái lưu trữ tên người dùng nhập
    var password by remember { mutableStateOf("") }  // Trạng thái lưu trữ mật khẩu người dùng nhập
    var errorMessage by remember { mutableStateOf("") }  // Trạng thái lưu trữ thông báo lỗi

    // Các giá trị hợp lệ cho tên và mật khẩu để kiểm tra đăng nhập
    val validName = "anhkha204"
    val validPassword = "204"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(16.dp)
    ) {
        // Nút "Back" để quay lại màn hình trước đó
        Button(
            onClick = { navController.popBackStack() },  // Điều hướng quay lại màn hình trước đó
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(text = "Back", color = Color.Black)
        }

        // Cột chứa các thành phần UI chính của màn hình đăng nhập
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tiêu đề đăng nhập
            Text(
                text = "LOGIN",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )

            // Button nhập tên tài khoản
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tài khoản") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFA726)
                )
            )

            // Button nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),  // Ẩn mật khẩu khi nhập
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFFFA726),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFA726)
                )
            )

            // Kiểm tra nếu có thông báo lỗi, hiển thị nó
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Nút đăng nhập
            Button(
                onClick = {
                    // Kiểm tra nếu cả tên và mật khẩu đều không trống
                    if (name.isNotEmpty() && password.isNotEmpty()) {
                        // Kiểm tra nếu tên và mật khẩu đúng
                        if (name == validName && password == validPassword) {
                            try {
                                // Điều hướng đến MainForm sau khi đăng nhập thành công
                                navController.navigate("mainForm")
                            } catch (e: Exception) {
                                // Hiển thị thông báo lỗi nếu có vấn đề với điều hướng
                                errorMessage = "Có lỗi khi điều hướng: ${e.localizedMessage}"
                            }
                        } else {
                            // Hiển thị thông báo lỗi nếu tên hoặc mật khẩu không đúng
                            errorMessage = "Email hoặc mật khẩu không đúng"
                        }
                    } else {
                        // Hiển thị thông báo yêu cầu nhập đầy đủ thông tin
                        errorMessage = "Vui lòng điền đầy đủ thông tin"
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Đăng nhập", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}

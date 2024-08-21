package com.example.fontendpicshare

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingUser(navController: NavHostController) {
    // Biến trạng thái để kiểm soát hiển thị hộp thoại xác nhận đăng xuất
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Dark background color
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // User profile image
            Icon(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "User Profile Image",
                tint = Color.White,
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hiển thị tiêu đề chào mừng người dùng
            Text(
                text = "Welcome, Kha",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Các phần còn lại của giao diện cài đặt
            SettingsSection("Thiết lập Tiện ích", icon = R.drawable.maintenance) {
                SettingItem("Mời tham gia qua link", R.drawable.share)
                SettingItem("Phản hồi", R.drawable.email) {
                    navController.navigate("reportMail")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            SettingsSection("Xã Hội", icon = R.drawable.social) {
                SettingItem("Bạn Bè", R.drawable.group)
            }

            Spacer(modifier = Modifier.height(8.dp))

            SettingsSection("Tổng quát", icon = R.drawable.checklist) {
                SettingItem("Thay đổi mật khẩu", R.drawable.cycle) {
                    navController.navigate("changePassword")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            SettingsSection("Vùng nguy hiểm", icon = R.drawable.warning) {
                SettingItem("Đăng xuất", R.drawable.logout) {
                    showLogoutDialog = true // Hiển thị hộp thoại xác nhận đăng xuất
                }
                SettingItem("Xóa tài khoản", R.drawable.bin)
            }
        }

        // Hộp thoại xác nhận đăng xuất tùy chỉnh xuất hiện ở phía dưới
        if (showLogoutDialog) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), // Nền mờ
                contentAlignment = Alignment.BottomCenter // Định vị hộp thoại ở dưới cùng
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF121212))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bạn có chắc chắn muốn đăng xuất không?",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            navController.navigate("firstForm")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Đăng Xuất",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

                    TextButton(
                        onClick = { showLogoutDialog = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Hủy",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, icon: Int? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                )
            }
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        content() // Call the composable lambda provided
    }
}

@Composable
fun SettingItem(text: String, icon: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1F1F1F))
            .padding(16.dp)
            .clickable(onClick = onClick), // Sử dụng clickable để thực hiện hành động khi nhấn vào
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}

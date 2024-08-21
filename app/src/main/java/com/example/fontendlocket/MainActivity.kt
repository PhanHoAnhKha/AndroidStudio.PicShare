package com.example.fontendpicshare

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.fontendlocket.ui.theme.FontEndLocKetTheme

class MainActivity : ComponentActivity() {

    // Khai báo biến ActivityResultLauncher để khởi chạy Activity và nhận kết quả
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        // Khởi tạo ActivityResultLauncher để lắng nghe kết quả trả về từ Activity khác
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> if (result.resultCode == RESULT_OK) {
                // Xử lý kết quả trả về từ Activity khác khi thành công
                // Bạn có thể lấy dữ liệu từ result.data và thực hiện logic tại đây
            }
        }
        // Đặt nội dung giao diện của ứng dụng bằng Jetpack Compose
        setContent {
            // Áp dụng theme tùy chỉnh cho ứng dụng
            FontEndLocKetTheme {
                // Điều hướng trong ứng dụng
                AppNavigation()
            }
        }
    }
}


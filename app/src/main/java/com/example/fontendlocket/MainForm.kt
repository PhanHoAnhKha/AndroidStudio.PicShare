package com.example.fontendpicshare

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainForm(navController: NavHostController) {
    // Lấy ngữ cảnh (context) và vòng đời (lifecycleOwner) hiện tại của Activity
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // CameraProviderFuture dùng để quản lý việc cung cấp camera
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Biến trạng thái cho PreviewView và ImageCapture để hiển thị và chụp ảnh
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var camera: Camera? by remember { mutableStateOf(null) }

    // Trạng thái quyền truy cập camera
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Trạng thái chọn camera trước hoặc sau
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    // Trạng thái bật/tắt đèn flash
    var flashEnabled by remember { mutableStateOf(false) }

    // Sử dụng Coroutine để xử lý các công việc không đồng bộ
    val coroutineScope = rememberCoroutineScope()

    // Launcher để yêu cầu quyền truy cập camera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }  // Cập nhật trạng thái quyền truy cập camera
    )

    // Yêu cầu quyền truy cập camera nếu chưa có
    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Hàm để liên kết camera với vòng đời của Activity, dùng cameraSelector để chọn camera trước hoặc sau
    fun bindCameraUseCases(cameraProvider: ProcessCameraProvider, view: PreviewView?) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(view?.surfaceProvider)  // Liên kết bề mặt (surface) để hiển thị camera
        }

        // Khởi tạo cấu hình cho việc chụp ảnh
        imageCapture = ImageCapture.Builder().setFlashMode(ImageCapture.FLASH_MODE_OFF).build()

        try {
            // Hủy liên kết tất cả camera trước đó và liên kết lại camera hiện tại với vòng đời Activity
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    // Giao diện người dùng
    if (hasCameraPermission) {  // Kiểm tra xem người dùng đã cấp quyền camera hay chưa
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black)  // Hộp nền màu đen chiếm toàn bộ màn hình
        ) {
            // Khung hiển thị camera
            Box(
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .size(width = 350.dp, height = 450.dp)  // Kích thước khung camera
                    .background(Color.Gray)
            ) {
                // Thành phần Android để hiển thị PreviewView của camera
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        previewView.scaleType = PreviewView.ScaleType.FILL_CENTER  // Định dạng hiển thị của camera
                        previewView  // Trả về previewView
                    },
                    modifier = Modifier.fillMaxSize()
                ) { view ->
                    previewView = view  // Gán previewView cho biến trạng thái
                    coroutineScope.launch(Dispatchers.Main) {
                        // Liên kết camera khi khung đã được khởi tạo
                        val cameraProvider = cameraProviderFuture.get()
                        bindCameraUseCases(cameraProvider, view)
                    }
                }
            }

            // Dòng chứa các nút ở trên cùng
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Nút hồ sơ người dùng
                IconButton(
                    onClick = {
                        navController.navigate("settingUser")
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "User Profile",
                        tint = Color.White
                    )
                }

                // Nút chat
                IconButton(
                    onClick = { /* Xử lý hành động chat */ },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = "Chat",
                        modifier = Modifier.fillMaxSize().padding(4.dp),
                        tint = Color.White
                    )
                }
            }

            // Điều khiển ở dưới cùng: Flash, nút chụp, và chuyển đổi camera
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dòng chứa các nút điều khiển ở dưới cùng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút bật/tắt đèn flash
                    IconButton(
                        onClick = {
                            flashEnabled = !flashEnabled
                            camera?.cameraControl?.enableTorch(flashEnabled)  // Bật/tắt đèn flash
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (flashEnabled) R.drawable.flashon else R.drawable.flash
                            ),
                            contentDescription = "Flash",
                            tint = Color.White
                        )
                    }

                    // Nút chụp ảnh
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = {
                                // Thực hiện hành động chụp ảnh
                            },
                            modifier = Modifier.size(80.dp).background(Color.White, CircleShape)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(70.dp).background(Color(0xFFFFC107), CircleShape)
                            ) {
                                Box(
                                    modifier = Modifier.size(60.dp).background(Color.White, CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        // Dòng hiển thị văn bản và biểu tượng mũi tên lịch sử
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Lịch Sử",
                                color = Color.White,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.background(Color.Transparent)
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.down),
                                contentDescription = "History Arrow",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp).padding(top = 0.dp)
                            )
                        }
                    }

                    // Nút chuyển đổi camera trước và sau
                    IconButton(
                        onClick = {
                            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA  // Chuyển đổi camera
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }

                            // Liên kết lại camera khi đã chuyển đổi
                            coroutineScope.launch(Dispatchers.Main) {
                                val cameraProvider = cameraProviderFuture.get()
                                bindCameraUseCases(cameraProvider, previewView)
                            }
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "Switch Camera",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

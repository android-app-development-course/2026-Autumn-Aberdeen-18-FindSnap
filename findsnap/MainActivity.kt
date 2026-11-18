package com.example.findsnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppMainScreen()
            }
        }
    }
}

@Composable
fun AppMainScreen() {
    // 界面状态：0=登录, 1=注册, 2=成功
    var currentScreen by remember { mutableStateOf(0) }

    // 模拟数据库：保存注册过的账号密码
    var registeredUsername by remember { mutableStateOf("") }
    var registeredPassword by remember { mutableStateOf("") }

    // 登录界面输入状态
    var inputUsername by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var loginAttempts by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoginDisabled by remember { mutableStateOf(false) }

    when (currentScreen) {
        0 -> LoginScreen(
            username = inputUsername,
            onUsernameChange = { inputUsername = it },
            password = inputPassword,
            onPasswordChange = { inputPassword = it },
            errorMessage = errorMessage,
            isLoginDisabled = isLoginDisabled,
            onLoginClick = {
                if (registeredUsername.isEmpty()) {
                    errorMessage = "Please register first..."
                } else if (inputUsername == registeredUsername && inputPassword == registeredPassword) {
                    currentScreen = 2 // 登录成功
                } else {
                    loginAttempts++
                    if (loginAttempts >= 3) {
                        isLoginDisabled = true
                        errorMessage = "Too many failed attempts. Button disabled."
                    } else {
                        errorMessage = "Wrong password. Attempts left: ${3 - loginAttempts}"
                    }
                }
            },
            onRegisterClick = {
                errorMessage = ""
                currentScreen = 1 // 跳转注册界面
            }
        )
        1 -> RegisterScreen(
            username = inputUsername,
            onUsernameChange = { inputUsername = it },
            password = inputPassword,
            onPasswordChange = { inputPassword = it },
            onRegisterConfirm = {
                registeredUsername = inputUsername
                registeredPassword = inputPassword
                inputUsername = ""
                inputPassword = ""
                errorMessage = "Registration successful. Please login."
                currentScreen = 0 // 回到登录界面
            },
            onBackClick = { currentScreen = 0 }
        )
        2 -> SuccessScreen(username = registeredUsername)
    }
}

@Composable
fun LoginScreen(
    username: String, onUsernameChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    errorMessage: String, isLoginDisabled: Boolean,
    onLoginClick: () -> Unit, onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = username, onValueChange = onUsernameChange, label = { Text("Username") })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLoginClick, enabled = !isLoginDisabled) {
            Text("LOGIN")
        }
        TextButton(onClick = onRegisterClick) {
            Text("REGISTER")
        }
    }
}

@Composable
fun RegisterScreen(
    username: String, onUsernameChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    onRegisterConfirm: () -> Unit, onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = username, onValueChange = onUsernameChange, label = { Text("Username") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = onPasswordChange, label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRegisterConfirm) {
            Text("REGISTER")
        }
        TextButton(onClick = onBackClick) {
            Text("Back to Login")
        }
    }
}

@Composable
fun SuccessScreen(username: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login Successful", style = MaterialTheme.typography.headlineMedium)
        Text("Welcome $username!", style = MaterialTheme.typography.bodyLarge)
    }
}
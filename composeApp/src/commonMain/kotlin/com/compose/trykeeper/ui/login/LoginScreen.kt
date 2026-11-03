package com.compose.trykeeper.ui.login


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.trykeeper.AppTheme
import com.compose.trykeeper.getColorsTheme
import com.compose.trykeeper.presentacion.login.LoginViewModel
import moe.tlaster.precompose.PreComposeApp

@Composable
@Preview
fun LoginScreen(loginViewModel: LoginViewModel, onRegisterClick: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val colors = getColorsTheme()
    var showError by remember { mutableStateOf(false) }
    var visiblePassword by remember { mutableStateOf(false) }

    LaunchedEffect(loginViewModel.isError) {
        if (loginViewModel.isError) {
            showError = true
            kotlinx.coroutines.delay(3000)
            showError = false
        }
    }


    PreComposeApp {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("AureaFlora", fontSize = 32.sp, color = colors.textColor)
                    OutlinedTextField(
                        onValueChange = { username = it },
                        modifier = Modifier.height(58.dp),
                        singleLine = true,
                        value = username,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = colors.backgroundColor,
                            focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            textColor = colors.textColor,
                        ),
                        label = { Text("Usuario") })
                    OutlinedTextField(
                        onValueChange = { password = it },
                        modifier = Modifier.height(82.dp).padding(vertical = 12.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        value = password,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = colors.backgroundColor,
                            focusedIndicatorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            textColor = colors.textColor,
                        ),
                        visualTransformation = if (!visiblePassword) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            if (visiblePassword) {
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(30.dp).clickable {
                                        visiblePassword = !visiblePassword
                                    })
                            } else {
                                Icon(
                                    Icons.Default.VisibilityOff,
                                    contentDescription = "Icon",
                                    modifier = Modifier.size(30.dp).clickable {
                                        visiblePassword = !visiblePassword
                                    })
                            }
                        },
                        label = { Text("Contraseña") })
                    Button(
                        onClick = {
                            loginViewModel.login(username, password)
                        },
                        modifier = Modifier.height(42.dp).fillMaxWidth(0.78f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Iniciar Sesión", color = Color.White)
                    }
                    Text(
                        "¿No tienes cuenta? Registrate",
                        color = colors.textColor,
                        modifier = Modifier.padding(top = 12.dp).clickable(onClick = onRegisterClick)
                    )

                    if (showError) {
                        Snackbar(
                            modifier = Modifier.padding(8.dp),
                            backgroundColor = Color.Red,
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(
                                text = "Usuario o contraseña incorrectos",
                                color = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

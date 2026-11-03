package com.compose.trykeeper.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.trykeeper.AppTheme
import com.compose.trykeeper.model.Users
import com.compose.trykeeper.getColorsTheme
import com.compose.trykeeper.presentacion.register.RegisterViewModel
import moe.tlaster.precompose.PreComposeApp

@Composable
fun RegisterScreen(onRegisterClick: (Users) -> Unit = {}) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var cellular by remember { mutableStateOf("") }
    var codeSaler by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validateButton =
        name.isNotEmpty() && lastname.isNotEmpty() && cedula.isNotEmpty() && cellular.isNotEmpty() && codeSaler.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
    val colors = getColorsTheme()

    PreComposeApp {
        AppTheme {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    .padding(vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Registrate", fontSize = 32.sp, color = colors.textColor)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    onValueChange = { lastname = it },
                    value = lastname,
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = cedula,
                    onValueChange = { cedula = it },
                    label = { Text("Cedula") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = cellular,
                    onValueChange = { cellular = it },
                    label = { Text("Celular") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = codeSaler,
                    onValueChange = { codeSaler = it },
                    label = { Text("Codigo Vendedor") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.backgroundColor,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        textColor = colors.textColor,
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                Button(
                    onClick = {
                        val newUser = Users(
                            name = name,
                            lastName = lastname,
                            cedula = cedula,
                            celular = cellular,
                            codeSale = codeSaler,
                            userName = username,
                            password = password
                        )
                        onRegisterClick(newUser)
                    },
                    enabled = validateButton,
                    modifier = Modifier.fillMaxWidth(0.78f).padding(top = 12.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Registrar", color = Color.White)
                }
            }
        }
    }
}
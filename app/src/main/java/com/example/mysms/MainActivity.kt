@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.mysms

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysms.ui.theme.MySmsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.Manifest

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySmsTheme {
                var phoneNo by remember{
                    mutableStateOf("")
                }

                var message by remember{
                    mutableStateOf("")
                }

                GetPermissions(context = this)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text(
                        text = "SMS APP", style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold

                        )
                    )

                    OutlinedTextField(
                        value = phoneNo, onValueChange = {
                            phoneNo = it
                        },
                        label = {
                            Text(text = "Phone No.")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        )
                    )

                    OutlinedTextField(value = message, onValueChange = {
                        message = it
                    },
                        label = {
                            Text(text = "Message")
                        }
                    )
                    
                    ElevatedButton(onClick = {
                        sendSms(phoneNo,message,this@MainActivity)
                    },
                        modifier = Modifier.padding(5.dp)
                        ) {
                            Text(text = "Send",style = TextStyle(
                                fontSize = 20.sp
                            ))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun sendSms(phoneNo:String,message:String,context: Context){
    try{
        val smsManager =
            context.getSystemService<SmsManager>(SmsManager::class.java).createForSubscriptionId(1)
        smsManager.sendTextMessage(phoneNo,null,message,null,null)

        Toast.makeText(context,"Hi, Message Sent",Toast.LENGTH_LONG).show()
    } catch(e:Exception){
        Toast.makeText(context,"Hi, Error in Sending Message",Toast.LENGTH_LONG).show()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetPermissions(context: Context){
    val multiplePermissions = rememberMultiplePermissionsState(
        permissions =
        listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_NUMBERS
        )
    )

    DisposableEffect(key1 = multiplePermissions) {
        multiplePermissions.launchMultiplePermissionRequest()
        onDispose { }
        
    }
}

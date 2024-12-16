package com.datasite.luditotest.main


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import com.datasite.luditotest.navigation.RootNavigationGraph
import com.datasite.luditotest.systemBar.SystemBarColorChanger
import com.datasite.luditotest.ui.theme.LUDITOTestTheme
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LUDITOTestTheme {
                SystemBarColorChanger(
                    statusBarColor = Color.White,
                    navigationBarColor = Color.White,
                    isLightIcons = false
                )
                RootNavigationGraph()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        Log.d("lifecyclell", "onStart: ")
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
        Log.d("lifecyclell", "onStop: ")
    }

    override fun onDestroy() {
        MapKitFactory.getInstance().onStop()
        super.onDestroy()
        Log.d("lifecyclell", "onDestroy: ")
    }
}

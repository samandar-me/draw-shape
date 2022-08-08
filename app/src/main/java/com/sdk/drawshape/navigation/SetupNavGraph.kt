package com.sdk.drawshape.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sdk.drawshape.helper.activityChooser
import com.sdk.drawshape.helper.checkAndAskPermission
import com.sdk.drawshape.helper.saveImage
import com.sdk.drawshape.ui.screens.AnimatedSplashScreen
import com.sdk.drawshape.ui.screens.HomeScreen
import com.sdk.drawshape.ui.components.Root
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(navController: NavHostController, activity: Activity) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Splash.route) {
            AnimatedSplashScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            Root(window = activity.window) {
                HomeScreen {
                    activity.checkAndAskPermission {
                        CoroutineScope(Dispatchers.IO).launch {
                            val uri = activity.saveImage(it)
                            withContext(Dispatchers.Main) {
                                activity.startActivity(activityChooser(uri))
                            }
                        }
                    }
                }
            }
        }
    }
}
package org.tues.sponti

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.tues.sponti.deeplink.DeepLinkEvent
import org.tues.sponti.deeplink.DeepLinkManager
import org.tues.sponti.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleDeepLink(intent)
        setContent {
            val navController = rememberNavController()

            AppNavGraph(navController = navController)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val uri: Uri = intent.data ?: return

        val token = uri.getQueryParameter("token")
        val email = uri.getQueryParameter("email")

        when (uri.host) {
            "forgot-password" -> {
                DeepLinkManager.handle(
                    DeepLinkEvent.ResetPassword(token, email)
                )
            }
        }
    }
}

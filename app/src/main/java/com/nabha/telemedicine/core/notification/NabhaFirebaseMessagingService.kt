package com.nabha.telemedicine.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NabhaFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle FCM message
        val title = remoteMessage.notification?.title ?: "Nabha Sehat"
        val body  = remoteMessage.notification?.body ?: ""
        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to Firestore for push delivery
    }

    private fun showNotification(title: String, body: String) {
        // Build and show local notification
    }
}

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Handle notification actions
    }
}

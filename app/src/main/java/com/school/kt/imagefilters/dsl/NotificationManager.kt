package com.school.kt.imagefilters.dsl

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.school.kt.imagefilters.R

class NotificationManager(val context: Context) {

    private val notification = notification(CHANNEL_ID) {
        smallIcon = R.drawable.bird_icon
        priority = NotificationCompat.PRIORITY_DEFAULT

        content {
            title = "Image Filter Notification"
            text = "Try this amazing application"
        }
    }

    fun showTestNotification() {
        // todo create channel only once (use preferences)
        //createNotificationChannel()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun notification(channelId: String, init: NotificationConfig.() -> Unit): Notification {
        with(NotificationCompat.Builder(context, channelId)) {
            with(NotificationConfig()) {
                apply(init)
                setSmallIcon(smallIcon)
                setPriority(priority)
                setContentTitle(content.title)
                setContentText(content.text)
            }
            return build()
        }
    }

    private class NotificationConfig {
        var content = ContentConfig()
            private set
        var priority = NotificationCompat.PRIORITY_DEFAULT
        var smallIcon: Int = R.drawable.ic_launcher_foreground

        fun content(init: ContentConfig.() -> Unit) = content.apply(init)
    }

    class ContentConfig {
        var title: String = ""
        var text: String = ""
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "test channel"
            val descriptionText = "test description"
            val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: android.app.NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "main"
        const val NOTIFICATION_ID = 1488
    }
}
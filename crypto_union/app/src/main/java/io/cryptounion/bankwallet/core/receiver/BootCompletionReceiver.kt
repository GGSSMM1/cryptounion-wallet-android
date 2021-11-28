package io.cryptounion.bankwallet.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.cryptounion.bankwallet.core.notifications.NotificationWorker

class BootCompletionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent?) {
        NotificationWorker.startPeriodicWorker(context)
    }
}

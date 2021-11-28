package io.cryptounion.bankwallet.modules.settings.notifications.bottommenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App
import io.horizontalsystems.coinkit.models.CoinType

object NotificationBottomMenuModule {

    class Factory(private val coinType: CoinType, private val coinName: String, private val mode: NotificationMenuMode) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = BottomNotificationsMenuViewModel(coinType, coinName, App.priceAlertManager, mode)
            return viewModel as T
        }
    }

}

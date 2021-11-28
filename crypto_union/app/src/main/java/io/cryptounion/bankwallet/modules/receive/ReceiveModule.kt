package io.cryptounion.bankwallet.modules.receive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.entities.Wallet
import io.cryptounion.bankwallet.modules.balance.NetworkTypeChecker

object ReceiveModule {

    class Factory(private val wallet: Wallet) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReceiveViewModel(wallet, App.adapterManager, NetworkTypeChecker(App.accountSettingManager)) as T
        }
    }

}

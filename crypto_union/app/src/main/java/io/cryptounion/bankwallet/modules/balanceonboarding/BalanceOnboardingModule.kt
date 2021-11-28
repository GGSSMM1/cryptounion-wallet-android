package io.cryptounion.bankwallet.modules.balanceonboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App

object BalanceOnboardingModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BalanceOnboardingViewModel(App.accountManager, App.walletManager) as T
        }
    }
}

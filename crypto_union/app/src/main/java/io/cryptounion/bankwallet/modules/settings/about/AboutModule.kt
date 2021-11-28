package io.cryptounion.bankwallet.modules.settings.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.ui.helpers.TextHelper

object AboutModule {
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AboutViewModel(App.appConfigProvider, TextHelper, App.termsManager, App.systemInfoManager) as T
        }
    }
}

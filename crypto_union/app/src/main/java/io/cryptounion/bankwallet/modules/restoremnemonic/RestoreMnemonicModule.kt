package io.cryptounion.bankwallet.modules.restoremnemonic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.core.managers.PassphraseValidator

object RestoreMnemonicModule {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = RestoreMnemonicService(App.wordsManager, PassphraseValidator())

            return RestoreMnemonicViewModel(service, listOf(service)) as T
        }
    }

}

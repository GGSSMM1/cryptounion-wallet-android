package io.cryptounion.bankwallet.core.managers

import io.cryptounion.bankwallet.core.IAccountManager
import io.cryptounion.bankwallet.core.ILocalStorage
import io.cryptounion.bankwallet.core.IWalletManager
import io.horizontalsystems.core.IKeyStoreCleaner

class KeyStoreCleaner(
        private val localStorage: ILocalStorage,
        private val accountManager: IAccountManager,
        private val walletManager: IWalletManager)
    : IKeyStoreCleaner {

    override var encryptedSampleText: String?
        get() = localStorage.encryptedSampleText
        set(value) {
            localStorage.encryptedSampleText = value
        }

    override fun cleanApp() {
        accountManager.clear()
        walletManager.enable(listOf())
        localStorage.clear()
    }
}

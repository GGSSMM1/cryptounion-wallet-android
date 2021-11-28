package io.cryptounion.bankwallet.modules.walletconnect.request.signmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.modules.walletconnect.WalletConnectService
import io.cryptounion.bankwallet.modules.walletconnect.WalletConnectSignMessageRequest

object WalletConnectSignMessageRequestModule {

    class Factory(
            private val signMessageRequest: WalletConnectSignMessageRequest,
            private val baseService: WalletConnectService
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return when (modelClass) {
                WalletConnectSignMessageRequestViewModel::class.java -> {
                    val service = WalletConnectSignMessageRequestService(signMessageRequest, baseService)
                    WalletConnectSignMessageRequestViewModel(service) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

}

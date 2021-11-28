package io.cryptounion.bankwallet.modules.walletconnect

import androidx.lifecycle.ViewModel
import io.cryptounion.bankwallet.core.Clearable

class WalletConnectViewModel(
        val service: WalletConnectService,
        private val clearables: List<Clearable>
) : ViewModel() {

    var sharedSendEthereumTransactionRequest: WalletConnectSendEthereumTransactionRequest? = null
    var sharedSignMessageRequest: WalletConnectSignMessageRequest? = null

    val initialScreen: InitialScreen
        get() = when (service.state) {
            WalletConnectService.State.Idle -> {
                InitialScreen.ScanQrCode
            }
            else -> {
                InitialScreen.Main
            }
        }

    override fun onCleared() {
        clearables.forEach {
            it.clear()
        }
        super.onCleared()
    }

    enum class InitialScreen {
        ScanQrCode, Main
    }
}
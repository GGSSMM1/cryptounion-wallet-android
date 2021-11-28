package io.cryptounion.bankwallet.modules.receive

import androidx.lifecycle.ViewModel
import io.cryptounion.bankwallet.core.IAdapterManager
import io.cryptounion.bankwallet.entities.Wallet
import io.cryptounion.bankwallet.entities.addressType
import io.cryptounion.bankwallet.modules.balance.NetworkTypeChecker

class ReceiveViewModel(
        wallet: Wallet,
        adapterManager: IAdapterManager,
        networkTypeChecker: NetworkTypeChecker) : ViewModel() {

    val receiveAddress: String
    val addressType: String?
    val testNet: Boolean

    init {
        val receiveAdapter = adapterManager.getReceiveAdapterForWallet(wallet) ?: throw NoReceiverAdapter()

        testNet = !networkTypeChecker.isMainNet(wallet)
        receiveAddress = receiveAdapter.receiveAddress
        addressType = wallet.configuredCoin.settings.derivation?.addressType()
    }

    class NoReceiverAdapter : Error("No Receiver Adapter")

}

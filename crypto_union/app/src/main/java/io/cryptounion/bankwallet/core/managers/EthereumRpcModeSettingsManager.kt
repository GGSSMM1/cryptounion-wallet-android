package io.cryptounion.bankwallet.core.managers

import io.cryptounion.bankwallet.core.IAdapterManager
import io.cryptounion.bankwallet.core.IBlockchainSettingsStorage
import io.cryptounion.bankwallet.core.IEthereumRpcModeSettingsManager
import io.cryptounion.bankwallet.core.IWalletManager
import io.cryptounion.bankwallet.entities.CommunicationMode
import io.cryptounion.bankwallet.entities.EthereumRpcMode
import io.horizontalsystems.coinkit.models.CoinType

class EthereumRpcModeSettingsManager(
        private val blockchainSettingsStorage: IBlockchainSettingsStorage,
        private val adapterManager: IAdapterManager,
        private val walletManager: IWalletManager
) : IEthereumRpcModeSettingsManager {

    private val coinType = CoinType.Ethereum

    override val communicationModes: List<CommunicationMode>
        get() = listOf(CommunicationMode.Infura)

    override fun rpcMode(): EthereumRpcMode {
        val storedRpcMode = blockchainSettingsStorage.ethereumRpcModeSetting(coinType)
        return storedRpcMode ?: EthereumRpcMode(coinType, CommunicationMode.Infura)
    }

    override fun save(setting: EthereumRpcMode) {
        blockchainSettingsStorage.saveEthereumRpcModeSetting(setting)

        val walletsForUpdate = walletManager.wallets.filter {
            it.coin.type == CoinType.Ethereum || it.coin.type is CoinType.Erc20
        }

        if (walletsForUpdate.isNotEmpty()) {
            adapterManager.refreshAdapters(walletsForUpdate)
        }
    }

}

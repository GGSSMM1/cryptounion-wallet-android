package io.cryptounion.bankwallet.core.managers

import io.cryptounion.bankwallet.core.IAdapterManager
import io.cryptounion.bankwallet.core.IBlockchainSettingsStorage
import io.cryptounion.bankwallet.core.IWalletManager
import io.cryptounion.bankwallet.entities.BitcoinCashCoinType
import io.horizontalsystems.coinkit.models.CoinType

class BitcoinCashCoinTypeManager(
        private val walletManager: IWalletManager,
        private val adapterManager: IAdapterManager,
        private val storage: IBlockchainSettingsStorage
) {

    private val defaultCoinType = BitcoinCashCoinType.type145

    val bitcoinCashCoinType: BitcoinCashCoinType
        get() {
            return storage.bitcoinCashCoinType ?: defaultCoinType
        }

    val hasActiveSetting: Boolean
        get() {
            return walletManager.wallets.firstOrNull { it.coin.type == CoinType.BitcoinCash } != null
        }

    fun save(bitcoinCashCoinType: BitcoinCashCoinType) {
        storage.bitcoinCashCoinType = bitcoinCashCoinType

        val walletsForUpdate = walletManager.wallets.filter { it.coin.type == CoinType.BitcoinCash }

        if (walletsForUpdate.isNotEmpty()) {
            adapterManager.refreshAdapters(walletsForUpdate)
        }
    }

    fun reset() {
        storage.bitcoinCashCoinType = null
    }
}

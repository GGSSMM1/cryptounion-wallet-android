package io.cryptounion.bankwallet.modules.blockchainsettings

import io.cryptounion.bankwallet.entities.AccountType.Derivation
import io.cryptounion.bankwallet.entities.BitcoinCashCoinType
import io.cryptounion.bankwallet.ui.extensions.BottomSheetSelectorViewItem
import io.horizontalsystems.coinkit.models.Coin

object BlockchainSettingsModule {

    data class Request(val coin: Coin, val type: RequestType)

    sealed class RequestType {
        class DerivationType(val derivations: List<Derivation>, val current: Derivation) : RequestType()
        class BitcoinCashType(val types: List<BitcoinCashCoinType>, val current: BitcoinCashCoinType) : RequestType()
    }

    data class Config(
            val coin: Coin,
            val title: String,
            val subtitle: String,
            val selectedIndexes: List<Int>,
            val viewItems: List<BottomSheetSelectorViewItem>,
            val description: String
    )

}

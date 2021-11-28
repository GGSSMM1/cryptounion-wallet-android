package io.cryptounion.bankwallet.modules.swap.settings.oneinch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.R
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.core.providers.Translator
import io.cryptounion.bankwallet.entities.Address
import io.cryptounion.bankwallet.modules.swap.SwapMainModule
import io.cryptounion.bankwallet.modules.swap.oneinch.OneInchTradeService
import io.cryptounion.bankwallet.modules.swap.settings.AddressResolutionService
import io.cryptounion.bankwallet.modules.swap.settings.RecipientAddressViewModel
import io.cryptounion.bankwallet.modules.swap.settings.SwapSlippageViewModel
import java.math.BigDecimal

object OneInchSwapSettingsModule {

    val defaultSlippage = BigDecimal("1")

    data class OneInchSwapSettings(
            var slippage: BigDecimal = defaultSlippage,
            var gasPrice: Long? = null,
            var recipient: Address? = null
    )

    sealed class State {
        class Valid(val swapSettings: OneInchSwapSettings) : State()
        object Invalid : State()
    }

    class Factory(
            private val tradeService: OneInchTradeService,
            private val blockchain: SwapMainModule.Blockchain
    ) : ViewModelProvider.Factory {

        private val service by lazy { OneInchSettingsService(tradeService.swapSettings) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val evmCoin = blockchain.coin!!

            return when (modelClass) {
                OneInchSettingsViewModel::class.java -> OneInchSettingsViewModel(service, tradeService) as T
                SwapSlippageViewModel::class.java -> SwapSlippageViewModel(service) as T
                RecipientAddressViewModel::class.java -> {
                    val addressParser = App.addressParserFactory.parser(evmCoin)
                    val resolutionService = AddressResolutionService(evmCoin.code, true)
                    val placeholder = Translator.getString(R.string.SwapSettings_RecipientPlaceholder)
                    RecipientAddressViewModel(service, resolutionService, addressParser, placeholder, listOf(service, resolutionService)) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}

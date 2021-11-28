package io.cryptounion.bankwallet.modules.swap.approve.confirmation

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.core.ethereum.EthereumFeeViewModel
import io.cryptounion.bankwallet.core.ethereum.EvmCoinServiceFactory
import io.cryptounion.bankwallet.core.ethereum.EvmTransactionService
import io.cryptounion.bankwallet.core.factories.FeeRateProviderFactory
import io.cryptounion.bankwallet.modules.sendevm.SendEvmData
import io.cryptounion.bankwallet.modules.sendevm.SendEvmModule
import io.cryptounion.bankwallet.modules.sendevmtransaction.SendEvmTransactionService
import io.cryptounion.bankwallet.modules.sendevmtransaction.SendEvmTransactionViewModel
import io.cryptounion.bankwallet.modules.swap.SwapMainModule
import io.horizontalsystems.core.findNavController

object SwapApproveConfirmationModule {

    class Factory(
            private val sendEvmData: SendEvmData,
            private val blockchain: SwapMainModule.Blockchain
    ) : ViewModelProvider.Factory {

        private val coin by lazy { blockchain.coin }
        private val evmKit by lazy { blockchain.evmKit!! }
        private val transactionService by lazy {
            val feeRateProvider = FeeRateProviderFactory.provider(coin)!!
            EvmTransactionService(evmKit, feeRateProvider, 20)
        }
        private val coinServiceFactory by lazy { EvmCoinServiceFactory(coin!!, App.coinKit, App.currencyManager, App.xRateManager) }
        private val sendService by lazy { SendEvmTransactionService(sendEvmData, evmKit, transactionService, App.activateCoinManager) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                SendEvmTransactionViewModel::class.java -> {
                    SendEvmTransactionViewModel(sendService, coinServiceFactory) as T
                }
                EthereumFeeViewModel::class.java -> {
                    EthereumFeeViewModel(transactionService, coinServiceFactory.baseCoinService) as T
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    fun start(fragment: Fragment, navigateTo: Int, navOptions: NavOptions, sendEvmData: SendEvmData) {
        val arguments = bundleOf(
                SendEvmModule.transactionDataKey to SendEvmModule.TransactionDataParcelable(sendEvmData.transactionData),
                SendEvmModule.additionalInfoKey to sendEvmData.additionalInfo
        )
        fragment.findNavController().navigate(navigateTo, arguments, navOptions)
    }

}

package io.cryptounion.bankwallet.modules.swap.confirmation.oneinch

import androidx.fragment.app.viewModels
import io.cryptounion.bankwallet.R
import io.cryptounion.bankwallet.core.AppLogger
import io.cryptounion.bankwallet.core.ethereum.EthereumFeeViewModel
import io.cryptounion.bankwallet.modules.sendevmtransaction.SendEvmTransactionViewModel
import io.cryptounion.bankwallet.modules.swap.confirmation.BaseSwapConfirmationFragment
import io.horizontalsystems.core.findNavController

class OneInchSwapConfirmationFragment : BaseSwapConfirmationFragment() {
    override val logger = AppLogger("swap_1inch")

    private val vmFactory by lazy { OneInchConfirmationModule.Factory(dex.blockchain, requireArguments()) }
    override val sendViewModel by viewModels<SendEvmTransactionViewModel> { vmFactory }
    override val feeViewModel by viewModels<EthereumFeeViewModel> { vmFactory }

    override fun navigateToFeeInfo() {
        findNavController().navigate(R.id.oneInchConfirmationFragment_to_feeSpeedInfo, null, navOptions())
    }

}

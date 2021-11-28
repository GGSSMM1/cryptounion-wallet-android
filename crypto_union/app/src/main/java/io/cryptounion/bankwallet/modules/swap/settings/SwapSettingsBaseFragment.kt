package io.cryptounion.bankwallet.modules.swap.settings

import androidx.navigation.navGraphViewModels
import io.cryptounion.bankwallet.R
import io.cryptounion.bankwallet.core.BaseFragment
import io.cryptounion.bankwallet.modules.swap.SwapMainModule
import io.cryptounion.bankwallet.modules.swap.SwapMainViewModel

abstract class SwapSettingsBaseFragment : BaseFragment() {
    private val mainViewModel by navGraphViewModels<SwapMainViewModel>(R.id.swapFragment)

    val dex: SwapMainModule.Dex
        get() = mainViewModel.dex

}

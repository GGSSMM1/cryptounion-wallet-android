package io.cryptounion.bankwallet.modules.balance

import io.cryptounion.bankwallet.core.Clearable
import io.cryptounion.bankwallet.core.IRateAppManager

class RateAppService(private val rateAppManager: IRateAppManager) : Clearable {

    fun onBalancePageActive() {
        rateAppManager.onBalancePageActive()
    }

    fun onBalancePageInactive() {
        rateAppManager.onBalancePageInactive()
    }

    override fun clear() = Unit
}

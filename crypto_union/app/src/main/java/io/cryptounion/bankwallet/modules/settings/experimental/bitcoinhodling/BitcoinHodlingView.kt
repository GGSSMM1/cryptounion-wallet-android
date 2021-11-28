package io.cryptounion.bankwallet.modules.settings.experimental.bitcoinhodling

import io.horizontalsystems.core.SingleLiveEvent

class BitcoinHodlingView : BitcoinHodlingModule.IView {
    val lockTimeEnabledLiveEvent = SingleLiveEvent<Boolean>()

    override fun setLockTime(enabled: Boolean) {
        lockTimeEnabledLiveEvent.postValue(enabled)
    }

}

package io.cryptounion.bankwallet.modules.settings.experimental

import io.horizontalsystems.core.SingleLiveEvent

class ExperimentalFeaturesRouter : ExperimentalFeaturesModule.IRouter {

    val showBitcoinHodlingLiveEvent = SingleLiveEvent<Unit>()

    override fun showBitcoinHodling() {
        showBitcoinHodlingLiveEvent.call()
    }

}

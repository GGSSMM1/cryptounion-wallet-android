package io.cryptounion.bankwallet.modules.tor

import io.cryptounion.bankwallet.core.managers.TorStatus
import io.horizontalsystems.core.SingleLiveEvent

class TorStatusView: TorStatusModule.View {

    val torConnectionStatus = SingleLiveEvent<TorStatus>()

    override fun updateConnectionStatus(connectionStatus: TorStatus) {
        torConnectionStatus.postValue(connectionStatus)
    }
}
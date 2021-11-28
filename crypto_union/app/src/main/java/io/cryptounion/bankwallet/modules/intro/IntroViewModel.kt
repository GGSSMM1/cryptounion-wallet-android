package io.cryptounion.bankwallet.modules.intro

import androidx.lifecycle.ViewModel
import io.cryptounion.bankwallet.core.ILocalStorage
import io.horizontalsystems.core.SingleLiveEvent

class IntroViewModel(
        private val localStorage: ILocalStorage
): ViewModel() {

    val openMainLiveEvent = SingleLiveEvent<Unit>()

    fun onClickStart() {
        localStorage.mainShowedOnce = true
        openMainLiveEvent.call()
    }

}

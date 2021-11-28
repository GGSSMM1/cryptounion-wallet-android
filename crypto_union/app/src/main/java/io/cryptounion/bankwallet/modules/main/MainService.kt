package io.cryptounion.bankwallet.modules.main

import io.cryptounion.bankwallet.core.ILocalStorage
import io.cryptounion.bankwallet.core.utils.RootUtil

class MainService(
        private val rootUtil: RootUtil,
        private val localStorage: ILocalStorage
) {

    val isDeviceRooted: Boolean
        get() = rootUtil.isRooted

    val ignoreRootCheck: Boolean
        get() = localStorage.ignoreRootedDeviceWarning

}

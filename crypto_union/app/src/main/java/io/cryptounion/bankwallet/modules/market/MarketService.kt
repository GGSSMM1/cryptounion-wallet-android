package io.cryptounion.bankwallet.modules.market

import io.cryptounion.bankwallet.core.IMarketStorage

class MarketService(private val storage: IMarketStorage) {

    var currentTab: MarketModule.Tab?
        get() = storage.currentTab
        set(value) {
            storage.currentTab = value
        }

}

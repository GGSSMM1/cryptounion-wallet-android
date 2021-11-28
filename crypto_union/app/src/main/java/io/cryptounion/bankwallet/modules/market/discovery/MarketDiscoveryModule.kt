package io.cryptounion.bankwallet.modules.market.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.modules.market.list.MarketListService
import io.cryptounion.bankwallet.modules.market.list.MarketListViewModel

object MarketDiscoveryModule {

    class Factory : ViewModelProvider.Factory {
        val service by lazy { MarketDiscoveryService(App.xRateManager, App.backgroundManager) }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>) = when (modelClass) {
            MarketDiscoveryViewModel::class.java -> {
                MarketDiscoveryViewModel(service, listOf(service)) as T
            }
            MarketListViewModel::class.java -> {
                val listService = MarketListService(service, App.currencyManager)
                MarketListViewModel(listService, App.connectivityManager, listOf(listService)) as T
            }
            else -> throw IllegalArgumentException()
        }
    }
}

package io.cryptounion.bankwallet.modules.market.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.modules.market.list.MarketListService
import io.cryptounion.bankwallet.modules.market.list.MarketListViewModel

object MarketFavoritesModule {

    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = MarketFavoritesService(App.xRateManager, App.marketFavoritesManager, App.backgroundManager)
            val listService = MarketListService(service, App.currencyManager)
            return MarketListViewModel(listService, App.connectivityManager, listOf(listService, service)) as T
        }

    }

}


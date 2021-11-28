package io.cryptounion.bankwallet.modules.market.favorites

import io.cryptounion.bankwallet.core.Clearable
import io.cryptounion.bankwallet.core.IRateManager
import io.cryptounion.bankwallet.core.managers.MarketFavoritesManager
import io.cryptounion.bankwallet.modules.market.MarketItem
import io.cryptounion.bankwallet.modules.market.list.IMarketListFetcher
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.core.entities.Currency
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class MarketFavoritesService(
        private val rateManager: IRateManager,
        private val marketFavoritesManager: MarketFavoritesManager,
        private val backgroundManager: BackgroundManager
) : IMarketListFetcher, BackgroundManager.Listener, Clearable {

    private val dataUpdatedSubject = PublishSubject.create<Unit>()

    override val dataUpdatedAsync: Observable<Unit>
        get() = Observable.merge(marketFavoritesManager.dataUpdatedAsync, dataUpdatedSubject)

    init {
        backgroundManager.registerListener(this)
    }

    override fun willEnterForeground() {
        dataUpdatedSubject.onNext(Unit)
    }

    override fun clear() {
        backgroundManager.unregisterListener(this)
    }

    override fun fetchAsync(currency: Currency): Single<List<MarketItem>> {
        return Single.fromCallable {
            marketFavoritesManager.getAll().map { it.coinType }
        }
                .flatMap { coinTypes ->
                    rateManager.getCoinMarketList(coinTypes, currency.code)
                }
                .map {
                    it.map{ topMarket ->
                        MarketItem.createFromCoinMarket(topMarket, currency, null)
                    }
                }
    }
}

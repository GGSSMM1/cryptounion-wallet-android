package io.cryptounion.bankwallet.modules.networksettings

import io.cryptounion.bankwallet.core.Clearable
import io.cryptounion.bankwallet.core.managers.AccountSettingManager
import io.cryptounion.bankwallet.core.subscribeIO
import io.cryptounion.bankwallet.entities.Account
import io.cryptounion.bankwallet.entities.EvmNetwork
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class NetworkSettingsService(
    val account: Account,
    private val accountSettingManager: AccountSettingManager
) : Clearable {
    private val disposables = CompositeDisposable()

    private val itemsRelay = PublishSubject.create<List<Item>>()
    var items = listOf<Item>()
        private set(value) {
            field = value
            
            itemsRelay.onNext(value)
        }

    val itemsObservable: Observable<List<Item>>
        get() = itemsRelay

    init {
        accountSettingManager.ethereumNetworkObservable
            .subscribeIO {
                handleSettingsUpdated(it.first)
            }.let {
                disposables.add(it)
            }

        accountSettingManager.binanceSmartChainNetworkObservable
            .subscribeIO {
                handleSettingsUpdated(it.first)
            }.let {
                disposables.add(it)
            }
        
        syncItems()
    }

    private fun evmItem(blockchain: Blockchain, evmNetwork: EvmNetwork): Item {
        return Item(blockchain, evmNetwork.name)
    }

    private fun handleSettingsUpdated(account: Account) {
        if (account == this.account) {
            syncItems()
        }
    }

    private fun syncItems() {
        items = listOf(
            evmItem(Blockchain.Ethereum, accountSettingManager.ethereumNetwork(account)),
            evmItem(Blockchain.BinanceSmartChain, accountSettingManager.binanceSmartChainNetwork(account))
        )
    }

    override fun clear() {
        disposables.clear()
    }

    enum class Blockchain {
        Ethereum, BinanceSmartChain
    }

    data class Item(val blockchain: Blockchain, val value: String)
}


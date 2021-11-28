package io.cryptounion.bankwallet.modules.balance

import io.cryptounion.bankwallet.core.IWalletManager
import io.cryptounion.bankwallet.core.managers.AccountSettingManager
import io.cryptounion.bankwallet.entities.Wallet
import io.reactivex.Observable

class BalanceActiveWalletRepository(
    private val walletManager: IWalletManager,
    private val accountSettingManager: AccountSettingManager
) {

    val itemsObservable: Observable<List<Wallet>> =
        Observable
            .merge(
                Observable.just(Unit),
                walletManager.activeWalletsUpdatedObservable,
                accountSettingManager.ethereumNetworkObservable,
                accountSettingManager.binanceSmartChainNetworkObservable
            )
            .map {
                walletManager.activeWallets
            }

    fun disable(wallet: Wallet) {
        walletManager.delete(listOf(wallet))
    }

    fun enable(wallet: Wallet) {
        walletManager.save(listOf(wallet))
    }

}

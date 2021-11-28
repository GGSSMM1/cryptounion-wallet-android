package io.cryptounion.bankwallet.core.factories

import io.cryptounion.bankwallet.core.IAccountFactory
import io.cryptounion.bankwallet.core.IAccountManager
import io.cryptounion.bankwallet.entities.Account
import io.cryptounion.bankwallet.entities.AccountOrigin
import io.cryptounion.bankwallet.entities.AccountType
import java.util.*

class AccountFactory(val accountManager: IAccountManager) : IAccountFactory {

    override fun account(type: AccountType, origin: AccountOrigin, backedUp: Boolean): Account {
        val id = UUID.randomUUID().toString()

        return Account(
                id = id,
                name = getNextWalletName(),
                type = type,
                origin = origin,
                isBackedUp = backedUp
        )
    }


    private fun getNextWalletName(): String {
        val count = accountManager.accounts.count()

        return "Wallet ${count + 1}"
    }
}

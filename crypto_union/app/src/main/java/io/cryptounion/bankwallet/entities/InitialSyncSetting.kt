package io.cryptounion.bankwallet.entities

import io.horizontalsystems.coinkit.models.CoinType

data class InitialSyncSetting(val coinType: CoinType,
                              var syncMode: SyncMode)

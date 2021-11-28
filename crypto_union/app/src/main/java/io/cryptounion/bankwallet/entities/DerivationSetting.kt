package io.cryptounion.bankwallet.entities

import io.cryptounion.bankwallet.entities.AccountType.Derivation
import io.horizontalsystems.coinkit.models.CoinType

class DerivationSetting(val coinType: CoinType,
                        var derivation: Derivation)

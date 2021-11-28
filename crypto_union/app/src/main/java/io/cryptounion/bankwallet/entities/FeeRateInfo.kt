package io.cryptounion.bankwallet.entities

import io.cryptounion.bankwallet.core.FeeRatePriority

data class FeeRateInfo(val priority: FeeRatePriority, var feeRate: Long, val duration: Long? = null)

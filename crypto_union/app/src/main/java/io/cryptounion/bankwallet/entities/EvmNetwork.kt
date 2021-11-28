package io.cryptounion.bankwallet.entities

import io.cryptounion.bankwallet.core.managers.url
import io.horizontalsystems.ethereumkit.core.EthereumKit

data class EvmNetwork(
    val name: String,
    val networkType: EthereumKit.NetworkType,
    val syncSource: EthereumKit.SyncSource
) {
    val id: String
        get() {
            val syncSourceUrl = syncSource.url.toString()

            return "${networkType.chainId}|$syncSourceUrl"
        }
}

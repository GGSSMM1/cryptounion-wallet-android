package io.cryptounion.bankwallet.modules.coin.tvlrank

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.cryptounion.bankwallet.R
import io.cryptounion.bankwallet.core.App
import io.horizontalsystems.xrateskit.entities.CoinData
import java.math.BigDecimal

object TvlRankModule {

    class Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TvlRankViewModel(
                App.numberFormatter,
                App.xRateManager,
                App.appConfigProvider
            ) as T
        }
    }

}

data class TvlRankViewItem(
    val data: CoinData,
    val tvl: String,
    val tvlDiff: BigDecimal,
    val tvlRank: String,
    val chains: String
)

enum class TvlRankSortField(@StringRes val titleResId: Int) {
    HighestTvl(R.string.TvlRank_Field_HighestTvl),
    LowestTvl(R.string.TvlRank_Field_LowestTvl)
}

enum class TvlRankFilterField(@StringRes val titleResId: Int) {
    All(R.string.TvlRank_Field_All),
    Ethereum(R.string.TvlRank_Field_Eth),
    Binance(R.string.TvlRank_Field_Bsc),
    Solana(R.string.TvlRank_Field_Sol),
    Avalanche(R.string.TvlRank_Field_Ava),
    Polygon(R.string.TvlRank_Field_Mat)
}

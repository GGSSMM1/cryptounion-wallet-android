package io.cryptounion.bankwallet.modules.swap.coinselect

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.cryptounion.bankwallet.core.App
import io.cryptounion.bankwallet.core.setCoinImage
import io.cryptounion.bankwallet.core.setOnSingleClickListener
import io.cryptounion.bankwallet.modules.swap.SwapMainModule.CoinBalanceItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_holder_swap_coin_select.*

class SelectSwapCoinViewHolder(
        override val containerView: View,
        val onClick: (item: CoinBalanceItem) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var coinItem: CoinBalanceItem? = null

    init {
        containerView.setOnSingleClickListener {
            coinItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(coinItem: CoinBalanceItem, showBottomBorder: Boolean) {
        this.coinItem = coinItem
        bottomShade.isVisible = showBottomBorder

        coinItem.apply {
            coinIcon.setCoinImage(coin.type)
            coinTitle.text = coin.title
            coinSubtitle.text = coin.code

            coinBalance.text = balance?.let {
                App.numberFormatter.formatCoin(it, coin.code, 0, 8)
            }

            fiatBalance.text = fiatBalanceValue?.let {
                App.numberFormatter.formatFiat(fiatBalanceValue.value, fiatBalanceValue.currency.symbol, 0, 2)
            }
        }
    }

}

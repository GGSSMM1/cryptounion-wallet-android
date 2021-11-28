package io.cryptounion.bankwallet.ui.selector

import android.view.ViewGroup
import io.cryptounion.bankwallet.R
import io.horizontalsystems.views.inflate

class SelectorItemViewHolderFactory<ItemClass> : ItemViewHolderFactory<ItemViewHolder<ViewItemWrapper<ItemClass>>> {
    override fun create(parent: ViewGroup, viewType: Int): ItemViewHolder<ViewItemWrapper<ItemClass>> {
        return SelectorItemViewHolder(inflate(parent, R.layout.view_holder_selector_item))
    }
}

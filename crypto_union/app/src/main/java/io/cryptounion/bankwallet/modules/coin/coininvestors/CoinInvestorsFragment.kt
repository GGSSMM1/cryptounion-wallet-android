package io.cryptounion.bankwallet.modules.coin.coininvestors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import io.cryptounion.bankwallet.R
import io.cryptounion.bankwallet.core.BaseFragment
import io.cryptounion.bankwallet.modules.coin.CoinViewModel
import io.cryptounion.bankwallet.ui.helpers.LinkHelper
import io.horizontalsystems.core.findNavController
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class CoinInvestorsFragment : BaseFragment(), CoinInvestorCategoryAdapter.Listener {

    private val coinViewModel by navGraphViewModels<CoinViewModel>(R.id.coinFragment)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = getString(R.string.CoinPage_FundsInvested)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        coinViewModel.coinInvestors.observe(viewLifecycleOwner, {
            val investorsAdapter = CoinInvestorCategoryAdapter(it, this)
            recyclerView.adapter = investorsAdapter
        })
    }

    override fun onItemClick(url: String) {
        context?.let { ctx ->
            LinkHelper.openLinkInAppBrowser(ctx, url)
        }
    }
}

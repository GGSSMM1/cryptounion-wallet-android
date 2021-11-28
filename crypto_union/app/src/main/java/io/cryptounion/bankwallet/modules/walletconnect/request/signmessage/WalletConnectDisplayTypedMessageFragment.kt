package io.cryptounion.bankwallet.modules.walletconnect.request.signmessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.cryptounion.bankwallet.R
import io.cryptounion.bankwallet.core.BaseFragment
import io.cryptounion.bankwallet.modules.walletconnect.request.WalletConnectRequestModule
import io.horizontalsystems.core.findNavController
import kotlinx.android.synthetic.main.fragment_wallet_connect_display_typed_message.*

class WalletConnectDisplayTypedMessageFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet_connect_display_typed_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        messageToSign.text = arguments?.getString(WalletConnectRequestModule.TYPED_MESSAGE)
    }

}

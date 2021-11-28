package io.cryptounion.bankwallet.modules.settings.appstatus

import io.cryptounion.bankwallet.core.IClipboardManager

class AppStatusInteractor(
        private val appStatusService: AppStatusService,
        private val clipboardManager: IClipboardManager
) : AppStatusModule.IInteractor {

    override val status: Map<String, Any>
        get() = appStatusService.status

    override fun copyToClipboard(text: String) {
        clipboardManager.copyText(text)
    }

}

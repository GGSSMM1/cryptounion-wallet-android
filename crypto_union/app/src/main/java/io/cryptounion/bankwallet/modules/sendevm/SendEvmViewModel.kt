package io.cryptounion.bankwallet.modules.sendevm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.cryptounion.bankwallet.core.Clearable
import io.cryptounion.bankwallet.core.convertedError
import io.cryptounion.bankwallet.core.subscribeIO
import io.cryptounion.bankwallet.modules.swap.settings.Caution
import io.horizontalsystems.coinkit.models.Coin
import io.horizontalsystems.core.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class SendEvmViewModel(
        val service: SendEvmService,
        private val clearables: List<Clearable>
) : ViewModel() {
    private val disposable = CompositeDisposable()

    val proceedEnabledLiveData = MutableLiveData(false)
    val amountCautionLiveData = MutableLiveData<Caution?>(null)
    val proceedLiveEvent = SingleLiveEvent<SendEvmData>()

    val coin: Coin
        get() = service.coin

    init {
        service.stateObservable.subscribeIO { sync(it) }.let { disposable.add(it) }
        service.amountErrorObservable.subscribeIO { sync(it.orElse(null)) }.let { disposable.add(it) }

        sync(service.state)
    }

    fun onClickProceed() {
        (service.state as? SendEvmService.State.Ready)?.let { readyState ->
            proceedLiveEvent.postValue(readyState.sendData)
        }
    }

    private fun sync(state: SendEvmService.State) {
        proceedEnabledLiveData.postValue(state is SendEvmService.State.Ready)
    }

    private fun sync(amountError: Throwable?) {
        val caution = amountError?.convertedError?.let {
            val text = amountError.localizedMessage ?: amountError.javaClass.simpleName
            Caution(text, Caution.Type.Error)
        }
        amountCautionLiveData.postValue(caution)
    }

    override fun onCleared() {
        clearables.forEach(Clearable::clear)
        disposable.clear()
    }
}

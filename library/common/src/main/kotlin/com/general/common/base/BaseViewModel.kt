package com.general.common.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.general.model.common.ViewState
import com.general.navigation.NavigationCommand
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + Dispatchers.IO

    private val mCompositeDisposable = CompositeDisposable()

    open var defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    fun setDispatchers(dispatcher: CoroutineDispatcher) {
        defaultDispatcher = dispatcher
    }

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected

    fun setStatusConnection(isConnected: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _isConnected.emit(isConnected)
        }
    }

    private val _statusViewModel = MutableSharedFlow<ViewState<Boolean>>(replay = 0)
    val statusViewModel: SharedFlow<ViewState<Boolean>>
        get() = _statusViewModel

    fun setStatusViewModel(status: ViewState<Boolean>) {
        viewModelScope.launch(Dispatchers.Main) {
            _statusViewModel.emit(status)
        }
    }

    private val _actionNavigate = MutableSharedFlow<NavigationCommand>()
    val actionNavigate: SharedFlow<NavigationCommand>
        get() = _actionNavigate

    fun navigate(direction: NavDirections) {
        viewModelScope.launch(Dispatchers.Main) {
            _actionNavigate.emit(NavigationCommand.To(direction))
        }
    }

    fun navigateBack() {
        viewModelScope.launch(Dispatchers.Main) {
            _actionNavigate.emit(NavigationCommand.Back)
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
        job.cancel()
    }
}
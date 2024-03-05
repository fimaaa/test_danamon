package com.general.common.extension

import androidx.lifecycle.viewModelScope
import com.general.common.base.BaseViewModel
import com.general.model.common.UIText
import com.general.model.common.ViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.net.ssl.HttpsURLConnection

fun <T> BaseViewModel.safeApiCall(
    defaultDispatcher: CoroutineDispatcher = this.defaultDispatcher,
    callSuccess: suspend () -> T
) {
    if (!isConnected.value) {
        setStatusViewModel(ViewState.ERROR(msg = UIText.DynamicString("No Internet Connection")))
        return
    }

    setStatusViewModel(ViewState.LOADING)
    viewModelScope.launch(defaultDispatcher) {
        try {
            callSuccess()
            setStatusViewModel(ViewState.SUCCESS(true))
        } catch (e: Exception) {
            if (!isConnected.value) {
                setStatusViewModel(ViewState.ERROR(msg = UIText.DynamicString("Internet Not Connected")))
            } else {
                e.printStackTrace()
                setStatusViewModel(e.toViewState())
            }
        }
    }
}

fun BaseViewModel.safeApiCallIndependent(
    defaultDispatcher: CoroutineDispatcher = this.defaultDispatcher,
    callError: suspend (Throwable) -> Unit = {},
    callSuccess: suspend () -> Unit
) {
    if (!isConnected.value) {
        viewModelScope.launch(Dispatchers.Main) {
            callError(Throwable("No Internet Connection"))
        }
        return
    }
    viewModelScope.launch(defaultDispatcher) {
        try {
            callSuccess()
        } catch (t: Throwable) {
            t.printStackTrace()
            callError(t)
        }
    }
}

fun <T> BaseViewModel.safeApiCollect(
    defaultDispatcher: CoroutineDispatcher = this.defaultDispatcher,
    state: MutableStateFlow<ViewState<T>>,
    callSuccess: suspend (T) -> T = { it },
    callApi: suspend () -> Flow<ViewState<T>>
) {
    setStatusViewModel(ViewState.LOADING)
    if (!isConnected.value) {
        setStatusViewModel(ViewState.ERROR(msg = UIText.DynamicString("No Internet Connection")))
        return
    }

    viewModelScope.launch(defaultDispatcher) {
        try {
            callApi.invoke().safeCollect {
                println("TAG $it")
                when (it) {
                    is ViewState.LOADING -> {
                        state.value = it
                        setStatusViewModel(ViewState.LOADING)
                    }

                    is ViewState.ERROR -> {
                        state.value = it
                        setStatusViewModel(
                            ViewState.ERROR(
                                msg = it.msg,
                                code = it.code,
                                err = it.err,
                                errResponse = it.errResponse,
                                data = false
                            )
                        )
                    }

                    is ViewState.EMPTY -> {
                        state.value = it
                        setStatusViewModel(it)
                    }

                    is ViewState.SUCCESS -> {
//                        state.value = it
                        setStatusViewModel(ViewState.SUCCESS(true))
                        state.value = ViewState.SUCCESS(callSuccess.invoke(it.data))
                    }
                }
            }
        } catch (e: Exception) {
            setStatusViewModel(e.toViewState())
            state.emit(e.toViewState())
        }
    }
}

fun <T> BaseViewModel.safeApiCollectIndependent(
    defaultDispatcher: CoroutineDispatcher = this.defaultDispatcher,
    state: MutableStateFlow<ViewState<T>>,
    callSuccess: suspend (ViewState<T>) -> ViewState<T> = { it },
    callApi: suspend () -> Flow<ViewState<T>>
) {
    if (!isConnected.value) {
        state.value = ViewState.ERROR(
            code = HttpsURLConnection.HTTP_BAD_GATEWAY,
            msg = UIText.DynamicString("No Internet Connection"),
            data = when (state.value) {
                is ViewState.SUCCESS -> (state.value as ViewState.SUCCESS).data
                is ViewState.ERROR -> (state.value as ViewState.ERROR).data
                else -> null
            }
        )
    } else {
        viewModelScope.launch(defaultDispatcher) {
            callApi.invoke().safeCollect {
                val emitData = callSuccess.invoke(it)
                state.emit(emitData)
            }
        }
    }
}
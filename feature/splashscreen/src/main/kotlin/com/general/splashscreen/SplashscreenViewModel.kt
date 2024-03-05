package com.general.splashscreen

import com.general.common.base.BaseViewModel
import com.general.common.extension.safeApiCollectIndependent
import com.general.model.common.ViewState
import com.general.model.common.user.Member
import com.general.repository.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SplashscreenViewModel @Inject constructor(
    private val appPreference: SessionRepository
) : BaseViewModel() {
    private val _dataSetting =
        MutableStateFlow<ViewState<Member>>(ViewState.EMPTY())
    val dateSetting: StateFlow<ViewState<Member>>
        get() = _dataSetting

    fun getIsAlreadyLoggedIn() {
        _dataSetting.value = (ViewState.LOADING)
        safeApiCollectIndependent(
            state = _dataSetting,
            callApi = {
                flow parentFlow@{
                    emit(ViewState.LOADING)
                    val isLoggedIn = appPreference.isLoggedIn()
                    val dataUser = appPreference.getUserSession()
                    if (!isLoggedIn || (dataUser == null)) {
                        emit(ViewState.EMPTY())
                        return@parentFlow
                    }
                    emit(ViewState.SUCCESS(dataUser))
                }
            })
    }
}
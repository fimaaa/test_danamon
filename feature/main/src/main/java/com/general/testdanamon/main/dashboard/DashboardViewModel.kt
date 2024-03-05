package com.general.testdanamon.main.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.general.common.base.BaseViewModel
import com.general.common.provider.LocationProvider
import com.general.model.common.ViewState
import com.general.model.common.user.Member
import com.general.repository.repository.SessionRepository
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {
    suspend fun getMember(): Member? = sessionRepository.getUserSession()

    private val _refreshList = MutableLiveData<Boolean>()
    val refreshList: LiveData<Boolean>
        get() = _refreshList

    fun getListTaskAll() {}

    suspend fun getLocationIsOn(): Boolean = try {
        val temp = locationProvider.checkLocationSetting()?.locationSettingsStates
        true
    } catch (e: ApiException) {
        setStatusViewModel(ViewState.ERROR(err = e, code = e.statusCode))
        false
    }
}
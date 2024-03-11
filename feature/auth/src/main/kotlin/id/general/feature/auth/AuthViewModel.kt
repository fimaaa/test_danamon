package id.general.feature.auth

import androidx.lifecycle.viewModelScope
import com.general.common.base.BaseViewModel
import com.general.common.extension.safeApiCollect
import com.general.model.common.UIText
import com.general.model.common.ViewState
import com.general.model.common.user.Member
import com.general.repository.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: SessionRepository
) : BaseViewModel() {
    private val isLoggedIn = MutableStateFlow(false)

    private val _loginState = MutableStateFlow<ViewState<Member>>(ViewState.EMPTY())
    val loginState: StateFlow<ViewState<Member>>
        get() = _loginState

    init {
        viewModelScope.launch {
            isLoggedIn.value = repo.isLoggedIn()
        }
    }

    fun onNotAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoggedIn.value = false
            _loginState.value = ViewState.EMPTY()
        }
    }

    fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoggedIn.value = false
            _loginState.value = ViewState.EMPTY()
            repo.logout()
        }
    }

    fun validateLogin(username: String, password: String, deviceId: String) {
        when {
            username.isEmpty() -> _loginState.value = ViewState.ERROR(
                UIText.DynamicString("Username tidak boleh kosong"),
                ErrorType_LOGIN_PHONE_NUMBER_INVALID
            )
//            username.length < 8 -> _loginState.value = ViewState.ERROR(
//                UIText.DynamicString("Nomor handphone minimal delapan karakter"),
//                ErrorType_LOGIN_PHONE_NUMBER_INVALID
//            )
            password.isEmpty() -> _loginState.value = ViewState.ERROR(
                UIText.DynamicString("Password tidak boleh kosong"),
                ErrorType_LOGIN_PASSWORD_INVALID
            )
            password.length < 8 -> _loginState.value = ViewState.ERROR(
                UIText.DynamicString("Password minimal delapan karakter"),
                ErrorType_LOGIN_PASSWORD_INVALID
            )
            else -> {
                postLogin(username, password, deviceId)
            }
        }
    }

    private fun postLogin(username: String, password: String, deviceId: String) {
        safeApiCollect(
            state = _loginState
        ) {
            repo.login(username, password, deviceId)
        }
    }

    companion object {
        const val ErrorType_LOGIN_PHONE_NUMBER_INVALID = -1
        const val ErrorType_LOGIN_PASSWORD_INVALID = -2
    }
}
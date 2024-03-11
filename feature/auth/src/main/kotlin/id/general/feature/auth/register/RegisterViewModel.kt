package id.general.feature.auth.register

import com.general.common.base.BaseViewModel
import com.general.common.extension.safeApiCollect
import com.general.model.common.ViewState
import com.general.model.common.user.CreateMemberData
import com.general.repository.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : BaseViewModel() {
    var userName: String = ""
    var name: String = ""
    var userType: String = ""
    var phone: String = ""
    var password: String = ""
    var deviceId: String = ""

    private val _validateButton: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val validateButton: MutableStateFlow<Boolean>
        get() = _validateButton

    fun validateButton() {
        var validated = true
        if (userName.isEmpty()) {
            validated = false
        }
        if (name.isEmpty()) {
            validated = false
        }
        if (userType.isEmpty()) {
            validated = false
        }
        if (phone.isEmpty()) {
            validated = false
        }
        if (password.isEmpty()) {
            validated = false
        }

        _validateButton.value = validated
    }

    private val _validateRegister: MutableStateFlow<ViewState<List<Pair<Int, String>>>> =
        MutableStateFlow(ViewState.LOADING)
    val validateRegister: MutableStateFlow<ViewState<List<Pair<Int, String>>>>
        get() = _validateRegister

    fun validateRegister() {
        val listError = mutableListOf<Pair<Int, String>>()
        if (userName.isEmpty()) {
            listError.add(Pair(1, "Username Must no Empty"))
        }
        if (name.isEmpty()) {
            listError.add(Pair(2, "Name Must no Empty"))
        }
        if (userType.isEmpty()) {
            listError.add(Pair(3, "You Should Choose UserType"))
        }
        if (phone.isEmpty()) {
            listError.add(Pair(4, "Phone Number Must no Empty"))
        }
        if (phone.isNotEmpty() && phone.length < 8) {
            listError.add(Pair(4, ""))
        }
        if (password.isEmpty()) {
            listError.add(Pair(5, "Password Must no Empty"))
        }
        if (password.isNotEmpty() && password.length < 8) {
            listError.add(Pair(5, "Password Length Mus More than 7"))
        }

        if (listError.isNotEmpty()) {
            _validateRegister.value = ViewState.ERROR(data = listError)
            return
        }

        registerMember()
    }

    private fun registerMember() {
        safeApiCollect(state = _validateRegister) {
            flow {
                emit(ViewState.LOADING)
                sessionRepository.registerMember(CreateMemberData(
                    username = userName,
                    fullname = name,
                    password = password,
                    phoneNumber = phone
                ))

                emit(ViewState.SUCCESS(data = listOf<Pair<Int, String>>()))
            }
        }
    }
}
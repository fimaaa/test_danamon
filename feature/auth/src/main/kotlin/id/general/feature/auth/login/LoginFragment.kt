package id.general.feature.auth.login

import androidx.fragment.app.activityViewModels
import com.general.common.base.BaseBindingFragment
import com.general.common.extension.safeCollect
import com.general.common.helper.MacAddressHelper
import com.general.model.common.ViewState
import com.general.navigation.MainNavDirections
import com.general.navigation.NavigationCommand
import id.general.feature.auth.AuthViewModel
import id.general.feature.auth.AuthViewModel.Companion.ErrorType_LOGIN_PASSWORD_INVALID
import id.general.feature.auth.AuthViewModel.Companion.ErrorType_LOGIN_PHONE_NUMBER_INVALID
import id.general.feature.auth.databinding.FragmentLoginBinding

class LoginFragment : BaseBindingFragment<FragmentLoginBinding, LoginViewModel>() {
    private val sharedViewModel: AuthViewModel by activityViewModels()

    override fun onObserveAction() {
        super.onObserveAction()
        safeCollect(sharedViewModel.statusViewModel) { status ->
            viewModel.setStatusViewModel(status)
        }
        safeCollect(sharedViewModel.loginState) {
            binding.usernameLayout.error = null
            binding.passwordLayout.error = null
            when (it) {
                is ViewState.ERROR -> {
                    when (it.code) {
                        ErrorType_LOGIN_PHONE_NUMBER_INVALID -> binding.usernameLayout.error =
                            it.msg.asString(requireContext())
                        ErrorType_LOGIN_PASSWORD_INVALID -> binding.passwordLayout.error =
                            it.msg.asString(requireContext())
                    }
                }
                is ViewState.SUCCESS -> {
                    parentAction.changeDataMember(it.data)
                    parentAction.navigateModule(
                        NavigationCommand.To(MainNavDirections.navigateToMainApp())
                    )
                }
            }
        }
    }

    override fun onReadyAction() {
        binding.apply {
            forgotPasswordBtn.setOnClickListener {
                viewModel.navigate(
                    LoginFragmentDirections.actionLoginFragmentToForgotPassword()
                )
            }
            loginBtn.setOnClickListener {
                val username = username.text.toString()
                val password = password.text.toString()
                sharedViewModel.validateLogin(username, password, MacAddressHelper.getMacAddress(requireContext()))
            }
            activationBtn.setOnClickListener {
                viewModel.navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        parentAction.toolbarVisibility(false)
        sharedViewModel.clearData()
    }
}

package id.general.feature.auth.forgotpassword

import androidx.fragment.app.activityViewModels
import com.general.common.base.BaseBindingFragment
import id.general.feature.auth.AuthViewModel
import id.general.feature.auth.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : BaseBindingFragment<FragmentForgotPasswordBinding, ForgotPasswordViewModel>() {
    private val sharedViewModel: AuthViewModel by activityViewModels()

    override fun onReadyAction() {
        binding.nextBtn.setOnClickListener {
            parentAction.checkUpdate {
            }
        }
    }
}

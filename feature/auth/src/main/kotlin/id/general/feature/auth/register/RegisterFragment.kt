package id.general.feature.auth.register

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.general.common.base.BaseBindingFragment
import com.general.common.extension.safeCollect
import com.general.common.helper.MacAddressHelper
import com.general.model.common.ViewState
import id.general.feature.auth.AuthViewModel
import id.general.feature.auth.databinding.FragmentRegisterBinding
import android.R as DefaultR

class RegisterFragment : BaseBindingFragment<FragmentRegisterBinding, RegisterViewModel>() {
    private val sharedViewModel: AuthViewModel by activityViewModels()

    private var adapter: ArrayAdapter<String>? = null
    override fun onInitialization() {
        super.onInitialization()

        adapter = ArrayAdapter(
            requireContext(),
            DefaultR.layout.simple_spinner_item,
            arrayOf("User", "Admin")
        ).apply {
            setDropDownViewResource(DefaultR.layout.simple_spinner_dropdown_item)
        }

        viewModel.deviceId = MacAddressHelper.getMacAddress(requireContext())
    }

    override fun onInitialization(binding: FragmentRegisterBinding) = binding.apply {
        spinnerUserType.adapter = adapter

        etUsername.addTextChangedListener {
            viewModel.userName = it?.toString() ?: ""
            viewModel.validateButton()
        }
        etFullname.addTextChangedListener {
            viewModel.name = it?.toString() ?: ""
            viewModel.validateButton()
        }
        etPhoneNumber.addTextChangedListener {
            viewModel.phone = it?.toString() ?: ""
            viewModel.validateButton()
        }
        etPassword.addTextChangedListener {
            viewModel.password = it?.toString() ?: ""
            viewModel.validateButton()
        }
        spinnerUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.userType = spinnerUserType.selectedItem.toString()
                viewModel.validateButton()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.userType = ""
            }
        }
    }

    override fun onObserveAction() {
        super.onObserveAction()
        safeCollect(viewModel.validateButton) {
            binding.btnLogin.isEnabled = it
        }

        safeCollect(viewModel.validateRegister) {
            println("TAG VALIDAET REGISTER $it")
            binding.etUsername.error = null
            binding.etFullname.error = null
            binding.etPhoneNumber.error = null
            binding.etPassword.error = null
            if (it is ViewState.SUCCESS) {
                viewModel.navigateBack()
                return@safeCollect
            }

            if (it !is ViewState.ERROR) return@safeCollect
            it.data?.forEach { pair ->
                when (pair.first) {
                    1 -> binding.etUsername.error = pair.second
                    2 -> binding.etFullname.error = pair.second
//                    3 -> binding.spinnerUserType.er
                    4 -> binding.etPhoneNumber.error = pair.second
                    5 -> binding.etPassword.error = pair.second
                }
            }
        }
    }

    override fun onReadyAction() {
        binding.btnLogin.setOnClickListener {
            viewModel.validateRegister()
        }
    }
}
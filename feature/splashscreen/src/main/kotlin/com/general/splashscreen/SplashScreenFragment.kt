package com.general.splashscreen

import android.annotation.SuppressLint
import com.general.common.base.BaseBindingFragment
import com.general.common.extension.gone
import com.general.common.extension.safeCollect
import com.general.common.extension.visible
import com.general.common.util.DialogUtils
import com.general.model.common.ViewState
import com.general.navigation.MainNavDirections
import com.general.navigation.NavigationCommand
import com.general.splashscreen.databinding.FragmentSplashscreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment :
    BaseBindingFragment<FragmentSplashscreenBinding, SplashscreenViewModel>() {

    override fun onObserveAction() {
        super.onObserveAction()
        safeCollect(viewModel.dateSetting) {
            when (it) {
                is ViewState.LOADING -> {
                    binding.pbLoading.visible()
                }
                is ViewState.EMPTY -> {
                    binding.pbLoading.gone()
                    parentAction.navigateModule(
                        NavigationCommand.To(
                            MainNavDirections.navigateToAuth()
                        )
                    )
                }
                is ViewState.ERROR -> {
                    DialogUtils.showDefaultDialog(
                        requireContext(),
                        DialogUtils.DefaultDialogData(
                            title = "alert",
                            desc = it.msg.asString(requireContext()),
                            "Refresh",
                            "Clear Data"
                        ),
                        { dialog ->
                            dialog.dismiss()
                            viewModel.getIsAlreadyLoggedIn()
                        },
                        { dialog ->
                            dialog.dismiss()
                            parentAction.navigateModule(
                                NavigationCommand.To(
                                    MainNavDirections.navigateToMainApp()
                                )
                            )
                        }
                    )
                }
                is ViewState.SUCCESS -> {
                    binding.pbLoading.gone()
                    parentAction.changeDataMember(it.data)
//                    findNavController().navigate(MainNavDirections.navigateToMainApp())
                    parentAction.navigateModule(
                        NavigationCommand.To(
                            MainNavDirections.navigateToMainApp()
                        )
                    )
                }
            }
        }
    }

    override fun onReadyAction() {
        parentAction.checkUpdate {
            viewModel.getIsAlreadyLoggedIn()
        }
    }
}
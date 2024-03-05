package com.general.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.general.common.extension.safeCollect
import com.general.navigation.NavigationCommand
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseBindingFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    private val parentViewModel: BaseActivityViewModel by activityViewModels()
    protected lateinit var viewModel: VM

    open fun onInitialization(binding: VB) = binding.apply {
    }

    override fun onInitialization() {
        super.onInitialization()
        val vbType = (javaClass.genericSuperclass as ParameterizedType)
        val vmClass = vbType.actualTypeArguments[1] as Class<VM>

        viewModel = createViewModelLazy(vmClass.kotlin, { viewModelStore }).value
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val vbType = (javaClass.genericSuperclass as ParameterizedType)
        val vbClass = vbType.actualTypeArguments[0] as Class<VB>
        val method = vbClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, layoutInflater, container, false) as VB
        onInitialization(binding)
        return binding.root
    }

    override fun onObserveAction() {
        safeCollect(parentViewModel.isConnected) { isConnected ->
            viewModel.setStatusConnection(isConnected)
        }
        safeCollect(viewModel.statusViewModel) { status ->
            parentViewModel.setStatusViewModel(status)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actionNavigate.safeCollect { command ->
                when (command) {
                    is NavigationCommand.To -> findNavController().navigate(
                        command.directions,
                        getExtras()
                    )
                    is NavigationCommand.Back -> findNavController().navigateUp()
                    is NavigationCommand.ManualTo -> findNavController().navigate(command.directionId, command.bundle)
                }
            }
        }
    }

    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
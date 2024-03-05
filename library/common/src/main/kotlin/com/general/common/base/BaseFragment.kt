package com.general.common.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.general.common.R
import com.general.common.extension.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
abstract class BaseFragment() : Fragment() {
    lateinit var parentAction: BaseActivityView

    private lateinit var job: Job
//    private lateinit var mainView: BaseActivityView
    private var menuProvider: MenuProvider? = null

    open fun onInitialization() = Unit

    abstract fun onReadyAction()

    open fun onObserveAction() = Unit

    open fun onFragmentDestroyed() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        job = Job()
        onInitialization()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mainView.toolbarVisibility(toolbarShow)
        activity.hideKeyboard(view)
        onObserveAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onFragmentDestroyed()
//        mainView.resetToolbar()
//        mainView.resetOnBackPressed()
//        mainView.resetOnTouchEvent()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (requireActivity() is BaseActivityView) {
            parentAction = (requireActivity() as BaseActivityView)
        }
    }

    override fun onStart() {
        super.onStart()
        onReadyAction()
    }

    fun changeOrientation(orientation: Int) {
        requireActivity().requestedOrientation = orientation
    }

    class LoadingDialog : DialogFragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.basedialog_loading, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun setMenu(
        menuId: Int,
        onCustomCreateMenu: (menu: Menu) -> Unit = {},
        onMenuClicked: (MenuItem) -> Boolean = { false }
    ) {
        val menuHost: MenuHost = requireActivity()
        menuProvider?.let {
            menuHost.removeMenuProvider(it)
        }
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(menuId, menu)
                onCustomCreateMenu.invoke(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return onMenuClicked.invoke(menuItem)
            }
        }
        menuProvider?.let {
            menuHost.addMenuProvider(it, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }
}
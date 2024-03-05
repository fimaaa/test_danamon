package com.general.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.general.common.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.general.common.extension.getScreenDevice
import com.general.common.extension.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseBottomSheet<T : ViewBinding, V : BaseViewModel>(private val isFull: Boolean = false) :
    BottomSheetDialogFragment(), CoroutineScope {

    private lateinit var job: Job
    lateinit var binding: T

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (isFull) {
            val dialog: Dialog = super.onCreateDialog(savedInstanceState)
            dialog.setOnShowListener { dialogInterface ->
                val bottomSheetDialog = dialogInterface as BottomSheetDialog
                setupFullHeight(bottomSheetDialog)
            }
            dialog
        } else {
            super.onCreateDialog(savedInstanceState)
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
//        val bottomSheet =
//            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//        val layoutParams = bottomSheet.layoutParams
//        val windowHeight = getWindowHeight()
//        if (layoutParams != null) {
//            layoutParams.height = windowHeight
//        }
//        bottomSheet.layoutParams = layoutParams
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        return requireActivity().getScreenDevice().heightPixels
    }

    protected abstract fun getViewBinding(): T

    open fun onInitialization() = Unit

    abstract fun onReadyAction()

    open fun onObserveAction() = Unit

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.hideKeyboard(view)
        onInitialization()
        onObserveAction()
        onReadyAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
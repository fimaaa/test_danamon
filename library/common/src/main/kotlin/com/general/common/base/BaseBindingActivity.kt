package com.general.common.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {

    private var _binding: VB? = null
    protected val binding: VB
        get() {
            if (_binding == null) {
                getBinding()
            }
            return _binding!!
        }

    @Suppress("UNCHECKED_CAST")
    override fun onInitialization() {
        if (_binding == null) {
            getBinding()
            setContentView(binding.root)
        }
    }

    private fun getBinding() {
        val vbType = (javaClass.genericSuperclass as ParameterizedType)
        val vbClass = vbType.actualTypeArguments[0] as Class<VB>
        val method = vbClass.getMethod("inflate", LayoutInflater::class.java)
        _binding = method.invoke(null, layoutInflater) as VB
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
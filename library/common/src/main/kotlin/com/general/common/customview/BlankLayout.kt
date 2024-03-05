package com.general.common.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.general.common.R
import com.general.common.databinding.LayoutBlankBinding
import com.general.common.extension.Toast_Default
import com.general.common.extension.copyToClipboard
import com.general.common.extension.showSnackBar
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

@SuppressLint("CustomViewStyleable")
class BlankLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(
    context,
    attrs,
    defStyleAttr
) {
    val binding: LayoutBlankBinding

    init {
        binding = LayoutBlankBinding.inflate(LayoutInflater.from(context), this, true)
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.LayoutBlank, 0, 0)
            val img = styledAttributes.getResourceId(
                R.styleable.LayoutBlank_img,
                0
            )
            val text = styledAttributes.getString(
                R.styleable.LayoutBlank_text
            )
            val type = styledAttributes.getInt(
                R.styleable.LayoutBlank_blankType,
                0
            )
            setType(type)
            setImg(img)
            setText(text)
            styledAttributes.recycle()
        }
        binding.txtBlankDesc.setOnClickListener {
            context.copyToClipboard(binding.txtBlankDesc.text.toString(), "Error Detail")
            context.showSnackBar(binding.root, "Text Coped", Toast_Default)
        }
    }

    fun setType(errorCode: Int, message: String? = null, messageDesc: String? = null) {
        binding.apply {
            when (errorCode) {
                HttpURLConnection.HTTP_NOT_FOUND, HttpURLConnection.HTTP_NO_CONTENT -> {
                    imgBlank.isVisible = true
                    setImg(R.drawable.ic_data_not_found)
                    txtBlank.text =
                        context.getString(R.string.error_search_notfound, message ?: "data")
                    txtBlankDesc.isGone = true
                    setButtonVisibility(false)
                }
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    imgBlank.isVisible = true
                    setImg(R.drawable.ic_error_button)
                    txtBlank.text = context.getString(R.string.error_unauthorized)
                    btnBlank.text = context.getString(R.string.login)
                    btnBlank.setOnClickListener {
//                        it?.getParentActivity()?.login()
                    }
                    txtBlankDesc.isGone = true
                    setButtonVisibility(true)
                }
                HttpsURLConnection.HTTP_BAD_REQUEST, HttpsURLConnection.HTTP_FORBIDDEN -> {
                    imgBlank.isVisible = true
                    setImg(R.drawable.ic_error_nobutton)
                    txtBlank.text = message ?: context.getString(R.string.error_default)
                    txtBlankDesc.isGone = true
                    setButtonVisibility(false)
                }
                GENERAL_NO_DATA_CUSTOM -> {
                    imgBlank.isGone = true
                    txtBlank.text = message ?: context.getString(R.string.general_no_data_title, "Task")
                    txtBlankDesc.isVisible = true
                    txtBlankDesc.text = messageDesc ?: context.getString(R.string.general_no_data_desc)
                    btnBlank.isGone = true
                }
                GENERAL_NO_DATA -> {
                    imgBlank.isGone = true
                    txtBlank.text = context.getString(R.string.general_no_data_title, message)
                    txtBlankDesc.isVisible = true
                    txtBlankDesc.text = context.getString(R.string.general_no_data_desc)
                    btnBlank.isGone = true
                }
                else -> {
                    imgBlank.isVisible = true
                    setImg(R.drawable.ic_error_nobutton)
                    txtBlank.text = message ?: context.getString(R.string.error_default)
                    txtBlankDesc.isGone = messageDesc == null
                    txtBlankDesc.text = messageDesc
                    btnBlank.text = context.getString(R.string.retry)
                    setButtonVisibility(true)
                }
            }
        }
    }

    fun setImg(value: Int) {
        if (value != 0) {
            binding.imgBlank.setImageResource(value)
        }
    }

    fun setText(value: String?) {
        binding.txtBlank.text = value
    }

    fun setDesc(value: String?) {
        binding.txtBlankDesc.isVisible = value?.isNotEmpty() == true
        binding.txtBlankDesc.text = value
    }

    fun setOnClick(text: String, onClick: () -> Unit) {
        binding.apply {
            btnBlank.isVisible = true
            btnBlank.text = text
            btnBlank.setOnClickListener {
                onClick.invoke()
            }
        }
    }

    fun setButtonVisibility(isVisible: Boolean) {
        binding.btnBlank.isVisible = isVisible
    }

    companion object {
        const val GENERAL_NO_DATA = -404
        const val GENERAL_NO_DATA_CUSTOM = -405
    }
}
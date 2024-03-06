package com.general.common.util

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.general.common.R
import com.general.common.databinding.BasedialogAlertBinding
import com.general.common.databinding.CustomTwobuttonDialogBinding
import com.general.common.extension.gone
import com.general.common.extension.visible

object DialogUtils {

    data class DefaultDialogData(
        val title: String,
        val desc: String,
        val txtButton1: String,
        val txtButton2: String,
        val txtLink: String? = null,
        val iconVisibility: Boolean = false
    )

    private fun showDialog(
        dialogFragment: DialogFragment,
        isCancelable: Boolean = false,
        manager: FragmentManager
    ) {
        try {
            dialogFragment.isCancelable = isCancelable
            dialogFragment.show(manager, dialogFragment.tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showDialogAlert(
        context: Context,
        title: String,
        desc: String,
        img: Int? = null,
        listener: () -> Unit = {}
    ) {
        val view =
            LayoutInflater.from(context).inflate(R.layout.basedialog_alert, null as ViewGroup?)
        val binding = BasedialogAlertBinding.bind(view)
        with(binding) {
            titleDialog.text = title
            descDialog.text = desc
            imgDialog.isVisible = img != null
            img?.let {
                imgDialog.setImageResource(it)
            }

            val builder = AlertDialog.Builder(context)
            builder.setView(root)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)

            ivCloseBasedialogalert.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setOnDismissListener {
                listener.invoke()
            }
        }
    }

    fun showAlertDialog(
        mContext: Context,
        message: String? = null,
        title: String = mContext.getString(R.string.error_default),
        isIconShown: Boolean = false,
        onDismissListener: (() -> Unit)? = null
    ) {
        val layoutInflater: LayoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = BasedialogAlertBinding.inflate(layoutInflater).apply {
            titleDialog.text = title
            descDialog.text = message ?: mContext.getString(R.string.error_default)
            imgDialog.isVisible = isIconShown
        }
        val builder = AlertDialog.Builder(mContext)
        builder.setView(binding.root)
        onDismissListener?.let { listener ->
            builder.setOnDismissListener {
                listener.invoke()
            }
        }
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)
        binding.ivCloseBasedialogalert.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDefaultDialog(
        context: Context,
        defaultData: DefaultDialogData,
        listenerBtn1: (Dialog) -> Unit,
        listenerBtn2: (Dialog) -> Unit,
        listenerLink: ((Dialog) -> Unit)? = null
    ) {
        val binding =
            CustomTwobuttonDialogBinding.inflate(LayoutInflater.from(context), null, false)
        with(binding) {
            titleDialog.text = defaultData.title
            descDialog.text = defaultData.desc
            btn1Dialog.text = defaultData.txtButton1
            imgDialog.isVisible = defaultData.iconVisibility

            btn2Dialog.text = defaultData.txtButton2
            if (defaultData.txtLink.isNullOrEmpty()) {
                linkDialog.gone()
            } else {
                linkDialog.visible()
                linkDialog.text = defaultData.txtLink
            }
            val builder = AlertDialog.Builder(context)
            builder.setView(root)
            val dialog = builder.create()
            dialog.setCancelable(false)
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)
            listenerLink?.let { unit ->
                linkDialog.setOnClickListener {
                    unit.invoke(dialog)
                }
            }
            btn2Dialog.setOnClickListener {
                listenerBtn2.invoke(dialog)
            }
            btn1Dialog.setOnClickListener {
                listenerBtn1.invoke(dialog)
            }
        }
    }

    fun showDialogImage(
        mContext: Context,
        bitmap: Bitmap
    ) {
        val builder = AlertDialog.Builder(mContext)
        builder.setView(ImageView(mContext).apply {
            setImageBitmap(bitmap)
        })
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)
    }

    fun showDialogImage(
        mContext: Context,
        url: String,
        onFailed: (String) -> Unit
    ) {
        val builder = AlertDialog.Builder(mContext)

        val circularProgressDrawable = CircularProgressDrawable(mContext)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val imageView = ImageView(mContext).apply {
            setImageDrawable(circularProgressDrawable)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            adjustViewBounds = true
        }

        builder.setView(imageView)
        val dialog = builder.create()
        dialog.setCancelable(true)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded)

        Glide.with(mContext)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    dialog.dismiss()
                    onFailed.invoke(url)
                }
            })
    }
}
package com.general.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.general.common.databinding.BasedialogLoadingBinding
import com.general.common.extension.observe
import com.general.common.extension.safeCollect
import com.general.common.firebase.ForceUpdateChecker
import com.general.common.util.DialogUtils
import com.general.model.common.ViewState
import com.general.navigation.NavigationCommand
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.net.ssl.HttpsURLConnection

@AndroidEntryPoint
abstract class BaseActivity : LocaleAwareCompatActivity(), BaseActivityView {
    protected val baseViewModel: BaseActivityViewModel by viewModels()
    private val connectionLiveData: ConnectionLiveData by lazy { ConnectionLiveData(this) }
    private val updateChecker by lazy { ForceUpdateChecker(this) }

    private val loadingDialog: AlertDialog by lazy {
        val layoutInflater: LayoutInflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val bindingDialog =
            BasedialogLoadingBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(bindingDialog.root)
        builder.create().apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCanceledOnTouchOutside(false)
        }
    }

    private val resolutionLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            DialogUtils.showAlertDialog(
                this,
                "You're operation before failed, because location not turn on / not found",
                title = "Try Again"
            )
        } else {
            DialogUtils.showAlertDialog(this, "You need Turn on the location")
            // Location settings resolution failed
            // Handle accordingly, e.g., show an error message to the user
        }
    }

    private var registeredHandler: (() -> Boolean)? = null

    open fun onInitialization() = Unit

    open fun onReadyAction() = Unit

    open fun onObserveAction() = Unit

    var onUnAuthorized: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        // Change the locale
//        val configuration = Configuration(resources.configuration)
//        configuration.setLocale(Language.INDONESIA.toLocale())
//        resources.updateConfiguration(configuration, resources.displayMetrics)

        onBackPressedDispatcher.addCallback(this) {
            if (registeredHandler != null) {
                if (registeredHandler?.invoke() == true) {
                    return@addCallback
                }
            }
            isEnabled = false
            remove()
            onBackPressedDispatcher.onBackPressed()
        }
        onInitialization()
        observeActivity()
        onObserveAction()
        onReadyAction()
    }

    private fun observeActivity() {
        observe(connectionLiveData) { isConnected ->
            baseViewModel.setStatusConnection(isConnected)
        }
        safeCollect(baseViewModel.statusViewModel) {
            println("TAG BASE CTIVITY $it")
            when (it) {
                is ViewState.LOADING -> {
                    loadingDialog.show()
                }
                is ViewState.SUCCESS -> {
                    loadingDialog.dismiss()
                }
                is ViewState.ERROR -> {
                    loadingDialog.dismiss()
                    if (it.code == LocationSettingsStatusCodes.RESOLUTION_REQUIRED && it.err is ApiException) {
                        val resolvableApiException: ResolvableApiException =
                            if (it.err is ResolvableApiException) it.err as ResolvableApiException
                            else ResolvableApiException((it.err as ApiException).status)
                        val intentSenderRequest = IntentSenderRequest.Builder(resolvableApiException.resolution).build()
                        resolutionLauncher.launch(intentSenderRequest)
                        return@safeCollect
                    }
                    DialogUtils.showAlertDialog(this, it.msg.asString(this))
                    if (
                        ((it.code == HttpsURLConnection.HTTP_FORBIDDEN) || (it.code == HttpsURLConnection.HTTP_UNAUTHORIZED)) &&
                        onUnAuthorized != null) {
                        onUnAuthorized?.invoke()
                    }
                }
                is ViewState.EMPTY -> {
                    loadingDialog.dismiss()
                }
            }
        }
    }

    override fun toolbarVisibility(visible: Boolean) {
        if (visible) supportActionBar?.show() else supportActionBar?.hide()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    override fun setCustomOnBackPressed(listener: () -> Boolean) {
        registeredHandler = listener
    }

    override fun resetOnBackPressed() {
        registeredHandler = null
    }

    override fun checkUpdate(onSuccess: () -> Unit) {
        updateChecker.check(onSuccess)
    }

    override fun changeLanguage(language: String) {
        updateLocale(Locale(language))
    }

    override fun navigateModule(command: NavigationCommand) {
    }
}
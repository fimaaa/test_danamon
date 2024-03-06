package com.general.testdanamon

import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.general.common.base.BaseBindingActivity
import com.general.model.common.user.Member
import com.general.testdanamon.databinding.ActivityCrashBinding
import javax.inject.Inject
import com.general.common.R as commonR

class CrashActivity : BaseBindingActivity<ActivityCrashBinding>() {

    @Inject
    lateinit var caocConfig: CaocConfig.Builder

    override fun onInitialization() {
        super.onInitialization()
        val config: CaocConfig =
            CustomActivityOnCrash.getConfigFromIntent(intent) ?: caocConfig.get()

        binding.blankLayout.setText(
            if (BuildConfig.VARIANT != "release") {
                getString(commonR.string.error_default)
            } else {
                CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, intent)
            }
        )

        binding.blankLayout.setDesc(
            if (BuildConfig.VARIANT != "release") {
                CustomActivityOnCrash.getStackTraceFromIntent(intent)
            } else {
                CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, intent)
            }
        )

        if (config.isShowRestartButton && config.restartActivityClass != null) {
            binding.blankLayout.setOnClick("Restart App") {
                CustomActivityOnCrash.restartApplication(
                    this,
                    config
                )
            }
        } else {
            binding.blankLayout.setOnClick("Close App") {
                CustomActivityOnCrash.closeApplication(
                    this,
                    config
                )
            }
        }
    }

    override fun changeDataMember(member: Member) = Unit
    override fun handleIntentUrl(url: String) = Unit
}
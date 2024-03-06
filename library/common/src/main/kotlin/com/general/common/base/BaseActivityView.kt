package com.general.common.base

import com.general.model.common.user.Member
import com.general.navigation.NavigationCommand

interface BaseActivityView {
    fun toolbarVisibility(visible: Boolean)
    fun setCustomOnBackPressed(listener: () -> Boolean)
    fun resetOnBackPressed()
    fun changeLanguage(language: String)
    fun navigateModule(command: NavigationCommand)
    fun checkUpdate(onSuccess: () -> Unit)
    fun changeDataMember(member: Member)
    fun handleIntentUrl(url: String)
}
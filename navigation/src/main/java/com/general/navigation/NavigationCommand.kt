package com.general.navigation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections

/**
 * A simple sealed class to handle more properly
 * navigation from a [ViewModel]
 */
sealed class NavigationCommand {
    data class To(val directions: NavDirections) : NavigationCommand()
    data class ManualTo(val directionId: Int, val bundle: Bundle) : NavigationCommand()
    object Back : NavigationCommand()
}
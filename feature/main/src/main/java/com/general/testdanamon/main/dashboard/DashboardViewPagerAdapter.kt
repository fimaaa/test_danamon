package com.general.testdanamon.main.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.admin.user.ListUserFragment
import com.general.jsonplaceholder.photo.ListPhotoFragment
import com.general.model.common.UIText
import com.general.testdanamon.main.R

class DashboardViewPagerAdapter(
//    fragmentActivity: FragmentActivity
//    fragmentActivity: Fragment
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val isAdmin: Boolean = false
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = if (isAdmin) 2 else 1

    override fun createFragment(position: Int): Fragment = when (position) {
        1 -> ListUserFragment()
        else -> ListPhotoFragment()
    }

    fun getFragmentTitle(position: Int): UIText = when (position) {
        1 -> UIText.StringResource(R.string.list_user)
        else -> UIText.StringResource(R.string.list_photo)
    }
}
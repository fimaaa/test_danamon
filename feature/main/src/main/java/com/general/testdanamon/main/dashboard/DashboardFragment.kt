package com.general.testdanamon.main.dashboard

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.general.common.base.BaseBindingFragment
import com.general.common.extension.setCircleBackgroundFromText
import com.general.testdanamon.main.databinding.FragmentDashboardBinding
import com.general.testdanamon.main.databinding.TabDashboardBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import com.general.common.R as commonR

class DashboardFragment : BaseBindingFragment<FragmentDashboardBinding, DashboardViewModel>() {
    private var adapter: DashboardViewPagerAdapter? = null
    private var tabLayoutMediator: TabLayoutMediator? = null
    override fun onInitialization(binding: FragmentDashboardBinding) = binding.apply {
        // Set Menu in Toolbar
        parentAction.toolbarVisibility(true)

        // Set Viewpager
        viewPager.offscreenPageLimit = 3

        viewLifecycleOwner.lifecycleScope.launch {
            val member = viewModel.getMember() ?: return@launch

            adapter = DashboardViewPagerAdapter(
                childFragmentManager,
                viewLifecycleOwner.lifecycle,
                member.memberType == "Admin"
            )
            viewPager.adapter = adapter

            // Set Viewpager with Tab
            tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                updateTabLayout(tab, position)
            }
            tabLayoutMediator?.attach()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.customView?.let { changeTabLayoutColor(it, true) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.customView?.let { changeTabLayoutColor(it, false) }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            tabLayout.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    commonR.color.color_primary
                )
            )
        }
    }

    override fun onReadyAction() {
        viewModel.getListTaskAll()
    }

    private fun updateTabLayout(tab: TabLayout.Tab, position: Int, taskCount: Int = 0) {
        tab.customView = null
        val customTab = TabDashboardBinding.inflate(layoutInflater)
        customTab.tabLayoutTitle.text =
            adapter?.getFragmentTitle(position)?.asString(requireContext())
        customTab.tabLayoutBadge.text = if (taskCount > 0) taskCount.toString() else "-"
        viewLifecycleOwner.lifecycleScope.launch {
            customTab.tabLayoutBadge.setCircleBackgroundFromText(
                "#" + ContextCompat.getColor(
                    requireContext(),
                    commonR.color.colorError
                )
            )
            changeTabLayoutColor(customTab.root, tab.isSelected)
        }
        customTab.tabLayoutBadge.isGone = taskCount == 0
        tab.customView = customTab.root
    }

    private fun changeTabLayoutColor(tab: View, isSelected: Boolean) {
        val tabTextView: TextView? = if (tab is TextView) {
            tab
        } else {
            try {
                val tabBinding = TabDashboardBinding.bind(tab)
                tabBinding.tabLayoutTitle
            } catch (_: Exception) {
                null
            }
        }
        if (isSelected) {
            tabTextView?.setTextColor(Color.WHITE)
        } else {
            tabTextView?.setTextColor(
                ColorUtils.setAlphaComponent(
                    Color.WHITE,
                    128
                )
            )
        }
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}
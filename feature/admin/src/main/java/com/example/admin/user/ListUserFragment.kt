package com.example.admin.user

import android.os.Build
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import com.example.admin.R
import com.example.admin.databinding.FragmentUserListBinding
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.general.common.base.BaseBindingFragment
import com.general.common.base.BaseLoadStateAdapter
import com.general.common.customview.BlankLayout
import com.general.common.extension.gone
import com.general.common.extension.observe
import com.general.common.extension.showSnackBar
import com.general.common.extension.toThrowableCode
import com.general.common.extension.toThrowableMessage
import com.general.common.extension.visible
import com.general.model.common.ViewState
import javax.net.ssl.HttpsURLConnection
import com.general.common.R as commonR

class ListUserFragment : BaseBindingFragment<FragmentUserListBinding, ListUserViewModel>() {
    private var skeleton: Skeleton? = null

    private lateinit var adapter: UserPagingAdapter

    override fun onInitialization(binding: FragmentUserListBinding) = binding.apply {
        adapter = UserPagingAdapter { member ->
            requireContext().showSnackBar(
                binding.root,
                member.userName
            )
        }.apply {
            withLoadStateHeaderAndFooter(
                header = BaseLoadStateAdapter { retry() },
                footer = BaseLoadStateAdapter { retry() }
            )
        }
        rcvPhoto.adapter = adapter
        skeleton =
            binding.rcvPhoto.applySkeleton(R.layout.item_user_admin).apply {
                maskColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireContext().getColor(R.color.white)
                } else {
                    ContextCompat.getColor(requireContext(), R.color.white)
                }
                showShimmer = true
            }
    }

    override fun onObserveAction() {
        super.onObserveAction()
        observe(viewModel.listJsonPhoto) {
            binding.swipeRefresh.isRefreshing = false
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                blankLayout.gone()
                rcvPhoto.visible()
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        if (adapter.itemCount < 1) skeleton?.showSkeleton()
                    }

                    is LoadState.Error -> {
                        if (skeleton?.isSkeleton() == true) skeleton?.showOriginal()
                        val throwable = (loadState.source.refresh as LoadState.Error).error
                        val throwableCode = throwable.toThrowableCode()
                        val throwableMessage = throwable.toThrowableMessage("Photo")
                        rcvPhoto.gone()
                        blankLayout.apply {
                            visible()
                            setType(
                                throwableCode,
                                throwableMessage.asString(requireContext())
                            )
                            setOnClick(getString(commonR.string.retry)) {
                                adapter.retry()
                            }
                        }
                        if (
                            (throwableCode == HttpsURLConnection.HTTP_FORBIDDEN) ||
                            (throwableCode == HttpsURLConnection.HTTP_UNAUTHORIZED)
                        ) {
                            viewModel.setStatusViewModel(
                                ViewState.ERROR(
                                    msg = throwableMessage,
                                    code = throwableCode,
                                    err = throwable
                                )
                            )
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (skeleton?.isSkeleton() == true) skeleton?.showOriginal()
                        if (
                            loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached &&
                            adapter.itemCount < 1
                        ) {
                            rcvPhoto.gone()
                            blankLayout.visible()
                            binding.blankLayout.setType(
                                BlankLayout.GENERAL_NO_DATA,
                                "Photo"
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onReadyAction() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
        binding.swipeRefresh.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        binding.swipeRefresh.isEnabled = false
    }

    override fun onDestroyView() {
        skeleton = null
        binding.rcvPhoto.adapter = null
        super.onDestroyView()
    }
}
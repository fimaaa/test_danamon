package com.general.library.unittest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.general.common.base.BaseViewModel
import com.general.library.unittest.util.TestCoroutineRule
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
abstract class BaseViewModelTestClass<VM : BaseViewModel> {
    @get:Rule
    val textInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    lateinit var viewModel: VM

    abstract fun initViewModel(): VM

    open fun init(): Unit = Unit

    @OptIn(ExperimentalStdlibApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = initViewModel()
        testCoroutineRule.runBlockingTest {
            this.coroutineContext[CoroutineDispatcher]?.let { viewModel.setDispatchers(it) }
            viewModel.setStatusConnection(true)
        }
        init()
    }

    @After
    fun teardown() {
        clearAllMocks()
        unmockkAll() // Unmock all mocked objects
    }
}
package com.general.testdanamon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.general.library.unittest.util.RoboShadow
import com.general.library.unittest.util.TestCoroutineRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 *
 * Pusing ah wwwwww
 *
 */

@HiltAndroidTest
@ExperimentalCoroutinesApi
// @RunWith(MockitoJUnitRunner::class)
@RunWith(RobolectricTestRunner::class)
@Config(shadows = [RoboShadow::class])
class ActivityMainTest {
//    @Mock
//    private lateinit var mActivityMain: ActivityMain

    private val mActivityMain: ActivityMain = mockk()

    @get:Rule
    val textInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
//        mockkObject(ActivityMain::class) // Mock the entire ActivityMain class
    }

    @After
    fun teardown() {
        clearAllMocks()
        unmockkAll() // Unmock all mocked objects
    }

    @Test
    fun addition_isCorrect() {
        textInstantTaskExecutorRule.run {
//            testCoroutineRule.runBlockingTest {
            // Given
//            every { mActivityMain.updateFCMToken("") } // Mock the behavior of methodA

            // When
//            mActivityMain.updateFCMToken("")
            Assert.assertEquals(4, 2 + 2)
//            }
        }
    }

    @Test
    fun clickingLogin_shouldStartLoginActivity() {
        Robolectric.buildActivity(ActivityMain::class.java).use { controller ->
            controller.setup() // Moves Activity to RESUMED state
//            mActivityMain = controller.get()

            mActivityMain.setIsNewNotification(true)
            Assert.assertEquals(true, mActivityMain.getIsNewNotification())
            mActivityMain.actionGetIdDataFromNotification("orderId")

            verify(exactly = 1) { mActivityMain.getDetailOrder("orderId") }
            verify(exactly = 1) { mActivityMain.setIsNewNotification(false) }
            verify(exactly = 0) { mActivityMain.addListOrderNotification(any()) }
//            activity.findViewById<TextView>(R.id.tv_logout).performClick()
//            val expectedIntent = Intent(activity, LoginFragment::class.java)
//            val actual: Intent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity()
//            assertEquals(expectedIntent.component, actual.component)
        }
    }

    @Test
    fun `getIdDataFromNotification should call getDetailOrder when isNewNotification is true`() {
        textInstantTaskExecutorRule.run {
//            testCoroutineRule.runBlockingTest {
            // Mock the behavior of the viewModel
//                coEvery { mActivityMain.viewModel.getDetailOrder("orderId") } returns Unit
//                coEvery { viewModelMock.listOrderNotification.add("orderId") } returns true

            mActivityMain.setIsNewNotification(true)
            Assert.assertEquals(true, mActivityMain.getIsNewNotification())
            mActivityMain.actionGetIdDataFromNotification("orderId")

            verify(exactly = 1) { mActivityMain.getDetailOrder("orderId") }
            verify(exactly = 1) { mActivityMain.setIsNewNotification(false) }
            verify(exactly = 0) { mActivityMain.addListOrderNotification(any()) }
//            }
        }
    }
}
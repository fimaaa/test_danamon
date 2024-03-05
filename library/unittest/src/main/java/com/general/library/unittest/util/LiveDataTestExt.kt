package com.general.library.unittest.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.mockito.Mockito

fun <T> LiveData<T>.observeTest(block: (Observer<T>) -> Unit) {
    val observer = Mockito.mock(Observer<T> { }.javaClass)
    try {
        observeForever(observer)
        block(observer)
    } finally {
        removeObserver(observer)
    }
}
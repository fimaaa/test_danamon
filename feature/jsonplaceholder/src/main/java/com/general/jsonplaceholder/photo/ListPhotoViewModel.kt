package com.general.jsonplaceholder.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.general.common.base.BaseViewModel
import com.general.repository.repository.JsonPlaceHolderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListPhotoViewModel @Inject constructor(
    private val jsonPlaceHolderRepository: JsonPlaceHolderRepository
) : BaseViewModel() {
    private val _refreshList = MutableLiveData<Boolean>()
    private val refreshList: LiveData<Boolean>
        get() = _refreshList

    fun refreshData() {
        _refreshList.value = !(refreshList.value ?: false)
    }

    val listJsonPhoto = refreshList.switchMap {
        jsonPlaceHolderRepository.showListPhotoPaging().cachedIn(viewModelScope)
    }
}
package com.thomas.apps.dailywallpaper.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.thomas.apps.dailywallpaper.network.NetworkService
import com.thomas.apps.dailywallpaper.repository.paging.BingImagePagingSource

class MainViewModel(private val service: NetworkService) : ViewModel() {

    val images = Pager(PagingConfig(pageSize = 7)) {
        BingImagePagingSource(service)
    }.flow.cachedIn(viewModelScope)
}
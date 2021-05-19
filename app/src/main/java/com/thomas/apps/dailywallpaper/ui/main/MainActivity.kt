package com.thomas.apps.dailywallpaper.ui.main

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.thomas.apps.dailywallpaper.adapter.ImageAdapter
import com.thomas.apps.dailywallpaper.databinding.ActivityMainBinding
import com.thomas.apps.dailywallpaper.network.NetworkService
import com.thomas.apps.dailywallpaper.utils.OnClickListener
import com.thomas.apps.dailywallpaper.utils.ViewUtils.viewGone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(NetworkService.create(application))
    }

    private val imageAdapter by lazy {
        ImageAdapter().apply {
            onDownloadClick = object : OnClickListener<ImageAdapter.ImageItem> {
                override fun invoke(item: ImageAdapter.ImageItem) {
                    Timber.i("download ${item.title}")
                }
            }

            onSetWallpaperClick = object : OnClickListener<ImageAdapter.ImageItem> {
                override fun invoke(item: ImageAdapter.ImageItem) {
                    Timber.i("set wallpaper ${item.title}")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()

        observe()

    }

    private fun observe() {
        with(viewModel) {
            lifecycleScope.launch {
                images.collectLatest { pagingData ->
                    val items = pagingData.map {
                        it.toImageItem()
                    }
                    imageAdapter.submitData(items)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = imageAdapter
        }

        lifecycleScope.launch {
            imageAdapter.loadStateFlow.collectLatest { loadStates ->
                //binding.progressCircular.viewGone(loadStates.refresh is LoadState.Loading)

//                retry.isVisible = loadState.refresh !is LoadState.Loading
//                errorMsg.isVisible = loadState.refresh is LoadState.Error
            }
        }
    }
}
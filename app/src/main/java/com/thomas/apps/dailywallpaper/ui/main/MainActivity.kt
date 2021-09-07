package com.thomas.apps.dailywallpaper.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.thomas.apps.dailywallpaper.adapter.ImageAdapter
import com.thomas.apps.dailywallpaper.databinding.ActivityMainBinding
import com.thomas.apps.dailywallpaper.network.NetworkService
import com.thomas.apps.dailywallpaper.utils.OnClickListener
import com.thomas.apps.dailywallpaper.worker.DownloadImageWork
import com.thomas.apps.dailywallpaper.worker.SetWallpaperWork
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var imageUrl: String? = null

    val requestPerm =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { grant ->
            if (grant) {
                imageUrl?.let { createDownloadWork(it) }
            }
        }

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(NetworkService.create(application))
    }

    private val imageAdapter by lazy {
        ImageAdapter().apply {
            onDownloadClick = object : OnClickListener<ImageAdapter.ImageItem> {
                override fun invoke(item: ImageAdapter.ImageItem) {
                    Timber.i("download ${item.title}")

                    imageUrl = item.imageUrl
                    if (Build.VERSION.SDK_INT <= 28)
                        requestPerm.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    else {
                        createDownloadWork(item.imageUrl)
                    }
                }
            }

            onSetWallpaperClick = object : OnClickListener<ImageAdapter.ImageItem> {
                override fun invoke(item: ImageAdapter.ImageItem) {
                    Timber.i("set wallpaper ${item.title}")

                    createSetWallpaperWork(item.imageUrl)
                }
            }
        }
    }

    private fun createSetWallpaperWork(imageUrl: String) {
        val setWallpaperWorkRequest =
            OneTimeWorkRequestBuilder<SetWallpaperWork>()
                .addTag("set wallpaper")
//                .setBackoffCriteria(
//                    BackoffPolicy.LINEAR,
//                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
//                    TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        DownloadImageWork.IMAGE_URL to imageUrl
                    )
                )
                .build()

        WorkManager
            .getInstance(this)
            .enqueueUniqueWork(imageUrl, ExistingWorkPolicy.KEEP, setWallpaperWorkRequest)
    }

    private fun createDownloadWork(imageUrl: String) {
        val downloadWorkRequest =
            OneTimeWorkRequestBuilder<DownloadImageWork>()
                .addTag("download image")
                .setInputData(
                    workDataOf(
                        DownloadImageWork.IMAGE_URL to imageUrl
                    )
                )
                .build()

        WorkManager
            .getInstance(this)
            .enqueueUniqueWork(imageUrl, ExistingWorkPolicy.KEEP, downloadWorkRequest)

//        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadWorkRequest.id)
//            .observe(this) {
//                when (it?.state) {
//                    WorkInfo.State.SUCCEEDED -> {
//                        toast("Download success")
//                    }
//                    WorkInfo.State.FAILED -> {
//                        toast("Download fail")
//                    }
//                    else -> {
//                        Timber.i("other state ${it.state.name}")
//                    }
//                }
//            }
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
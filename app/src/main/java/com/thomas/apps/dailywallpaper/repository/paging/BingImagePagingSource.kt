package com.thomas.apps.dailywallpaper.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.thomas.apps.dailywallpaper.model.Image
import com.thomas.apps.dailywallpaper.network.NetworkService
import timber.log.Timber

class BingImagePagingSource(private val service: NetworkService) : PagingSource<Int, Image>() {
    companion object {
        private const val START_PAGE = 0
    }

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: START_PAGE
//            val size = params.loadSize

            Timber.i("page: $nextPageNumber - ")
            val response = service.requestWallpaper(nextPageNumber)

            if (response.isSuccessful) {
                val body = response.body()
                val images = body?.images?.map { it.toImage() } ?: emptyList()

//                val nextKey = images.lastOrNull()?.getNexKey()
                val nextKey = if (nextPageNumber + 7 > 7) null else nextPageNumber + 7
                return LoadResult.Page(
                    data = images,
                    prevKey = null,
                    nextKey = nextKey,
                )
            } else {
                val throwable = Throwable(response.errorBody()?.string())
                return LoadResult.Error(throwable)
            }

        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            Timber.e(e)

            return LoadResult.Error(e)
        }
    }
}
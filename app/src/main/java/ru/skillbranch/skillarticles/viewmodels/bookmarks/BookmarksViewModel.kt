package ru.skillbranch.skillarticles.viewmodels.bookmarks

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.skillbranch.skillarticles.data.models.ArticleItemData
import ru.skillbranch.skillarticles.data.repositories.ArticleStrategy
import ru.skillbranch.skillarticles.data.repositories.ArticlesDataFactory
import ru.skillbranch.skillarticles.data.repositories.ArticlesRepository
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.Notify
import java.util.concurrent.Executors

class BookmarksViewModel(handle: SavedStateHandle) : BaseViewModel<BookmarksState>(handle, BookmarksState()) {

    private val repository = ArticlesRepository

    private val listConfig by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .setPrefetchDistance(30)
            .setInitialLoadSizeHint(50)
            .build()
    }
    private val listData = Transformations.switchMap(state) {
        when {
            it.isSearch && !it.searchQuery.isNullOrBlank() -> buildPagedList(
                repository.searchBookmarks(
                    it.searchQuery
                )
            )
            else -> buildPagedList(repository.allBookmarks())
        }
    }

    fun observeList(
        owner: LifecycleOwner,
        onChange: (list: PagedList<ArticleItemData>) -> Unit
    ) {
        listData.observe(owner, Observer {onChange(it)})
    }

    fun handleToggleBookmark(id: String, isBookmark: Boolean) {
        notify(Notify.TextMessage("toggle"))
        repository.updateBookmark(id, isBookmark)
        listData.value?.dataSource?.invalidate()
    }

    private fun buildPagedList(
        dataFactory: ArticlesDataFactory
    ) : LiveData<PagedList<ArticleItemData>> {
        val builder = LivePagedListBuilder<Int, ArticleItemData>(
            dataFactory,
            listConfig
        )

        if (dataFactory.strategy is ArticleStrategy.BookmarkArticles) {
            builder.setBoundaryCallback(
                BookmarksBoundaryCallback(
                    ::zeroLoadingHandle,
                    ::itemAtEndHandle
                )
            )
        }

        return builder
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
    }

    private fun itemAtEndHandle(lastLoadArticle: ArticleItemData) {
        viewModelScope.launch(Dispatchers.IO) {
            val items = repository.loadArticlesFromDb(
                start = lastLoadArticle.id.toInt().inc(),
                size = listConfig.pageSize
            )
            withContext(Dispatchers.Main) {
                notify(
                    Notify.TextMessage(
                        "Load from db articles from ${items.firstOrNull()?.id}" +
                                "to ${items.lastOrNull()?.id}"
                    )
                )
            }
        }
    }

    private fun zeroLoadingHandle() {
        notify(Notify.TextMessage("Storage is empty"))
        viewModelScope.launch(Dispatchers.IO) {
            val items = repository.loadArticlesFromDb(
                start = 0,
                size = listConfig.initialLoadSizeHint
            )
        }
    }

    fun handleSearch(query: String?) {
        query ?: return
        updateState { it.copy(searchQuery = query) }
    }

    fun handleSearchMode(isSearch: Boolean) {
        updateState { it.copy(isSearch = isSearch) }
    }
}

data class BookmarksState(
    val isSearch: Boolean = false,
    val searchQuery: String? = null,
    val isLoading: Boolean = true
) : IViewModelState

class BookmarksBoundaryCallback(
    private val zeroLoadingHandle: () -> Unit,
    private val itemAtEndHandle: (ArticleItemData) -> Unit
) : PagedList.BoundaryCallback<ArticleItemData>() {

    override fun onZeroItemsLoaded() {
        zeroLoadingHandle()
    }

    override fun onItemAtEndLoaded(itemAtEnd: ArticleItemData) {
        itemAtEndHandle(itemAtEnd)
    }
}
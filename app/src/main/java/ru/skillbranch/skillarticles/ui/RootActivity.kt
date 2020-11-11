package ru.skillbranch.skillarticles.ui

import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toolbar
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_bottombar.*
import kotlinx.android.synthetic.main.layout_bottombar.view.*
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.ui.article.IArticleView
import ru.skillbranch.skillarticles.ui.base.BaseActivity
import ru.skillbranch.skillarticles.ui.base.Binding
import ru.skillbranch.skillarticles.ui.delegates.AttrValue
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.base.Notify


class RootActivity : BaseActivity<ArticleViewModel>(), IArticleView {

    override val layout: Int = R.layout.activity_root

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override val binding: Binding by lazy {
        ArticleBinding()
    }

    override val viewModel: ArticleViewModel by provideViewModel("0");

    private var isSearch = false
    private var searchQuery: String? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val bgColor by AttrValue(R.attr.colorSecondary)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val fgColor by AttrValue(R.attr.colorOnSecondary)

    override fun setupViews() {
        setupToolbar()
        setupBottomBar()
        setupMenu()
    }

    override fun renderNotification(notify: Notify) {
        val snackbar = Snackbar.make(coordinator_container, notify.message, Snackbar.LENGTH_LONG)
            .setAnchorView(bottombar)
        when (notify) {
            is Notify.ActionMessage -> {
                val (_, label, handler) = notify
                with(snackbar) {
                    setActionTextColor(getColor(R.color.color_accent_dark))
                    setAction(label) {handler.invoke()}
                }
            }
            is Notify.ErrorMessage -> {
                val (_, label, handler) = notify
                with(snackbar) {
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    setActionTextColor(getColor(android.R.color.white))
                    handler ?: return@with
                    setAction(label) {handler.invoke()}
                }
            }
        }
        snackbar.show()
    }

    override fun renderSearchResult(searchResult: List<Pair<Int, Int>>) {
        tv_text_content.renderSearchResult(searchResult)
    }

    override fun renderSearchPosition(searchPosition: Pair<Int, Int>?) {
        tv_text_content.renderSearchPosition(searchPosition)
    }

    override fun clearSearchResult() {
        tv_text_content.clearSearchResult()
    }

    override fun showSearchBar() {
        bottombar.setSearchState(true)
        scroll.setMarginOptionally(bottom = dpToIntPx(56))
    }

    override fun hideSearchBar() {
        bottombar.setSearchState(false)
        scroll.setMarginOptionally(bottom = dpToIntPx(0))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView: SearchView? = searchItem?.actionView as? SearchView
        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(componentName))


        if (isSearch){
            searchItem?.expandActionView()
            searchView?.setQuery(searchQuery, false)
            searchView?.clearFocus()
        }


        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(false)
                return true
            }
        })

       searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearch(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val logo = if (toolbar.childCount < 2) null else toolbar.getChildAt(2) as? ImageView
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP
        val lp = logo?.layoutParams as? Toolbar.LayoutParams
        lp?.let {
            it.width = this.dpToIntPx(40)
            it.height = this.dpToIntPx(40)
            it.marginEnd = dpToIntPx(16)
            logo.layoutParams = it
        }
    }

    private fun setupBottomBar(){
        bottombar.btn_like.setOnClickListener {
            viewModel.handleLike()
        }
        bottombar.btn_bookmark.setOnClickListener {
            viewModel.handleBookmark()
        }
        bottombar.btn_share.setOnClickListener {
            viewModel.handleShare()
        }
        bottombar.btn_settings.setOnClickListener {
            viewModel.handleToggleMenu()
        }
        btn_result_up.setOnClickListener {
            if (tv_text_content.hasFocus()) tv_text_content.requestFocus()
//            hideKeyboard(btn_result_up)
//            viewModel.handleUpResult()
        }
        btn_result_down.setOnClickListener {
            if (tv_text_content.hasFocus()) tv_text_content.requestFocus()
//            hideKeyboard(btn_result_down)
//            viewModel.handleDownResult()
        }
    }

    private fun setupMenu(){
        switch_mode.setOnClickListener {
            delegate.localNightMode = if (switch_mode.isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        }
    }

    private fun setupCopyListener(){
        tv_text_content.setCopyListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied code", it)
            clipboard.setPrimaryClip(clip)
//            viewModel.handleCopyCode()
        }
    }



}
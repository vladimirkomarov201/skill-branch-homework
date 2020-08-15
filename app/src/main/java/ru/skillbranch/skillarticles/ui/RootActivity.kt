package ru.skillbranch.skillarticles.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottombar.view.*
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.extensions.getChildOrNull
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.ViewModelFactory


class RootActivity : AppCompatActivity() {

    private lateinit var viewModel: ArticleViewModel

    private var isSearch = false
    private var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, ViewModelFactory(""))[ArticleViewModel::class.java]
        viewModel.observeState(this) {
            if (it.isSearch){
                isSearch = true
                searchQuery = it.searchQuery
            }
            bottombar.btn_like.isChecked = it.isLike
            bottombar.btn_bookmark.isChecked = it.isBookmark
            bottombar.btn_settings.isChecked = it.isShowMenu
            if (it.isShowMenu) submenu.open() else submenu.close()
            tv_text_content.text = it.content.firstOrNull()?.toString() ?: ""
        }
        switch_mode.setOnClickListener {
            delegate.localNightMode = if (switch_mode.isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        }
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
        setupToolbar()
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
        val logo = toolbar.getChildOrNull(2) as? ImageView
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP
        val lp = logo?.layoutParams as? Toolbar.LayoutParams
        lp?.let {
            it.width = this.dpToIntPx(40)
            it.height = this.dpToIntPx(40)
            logo.layoutParams = it
        }
    }

}
package com.hendri.githubuser.ui.main.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.adapter.MainAdapter
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchManager: SearchManager
    private lateinit var keyword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyword = "he"
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(MainViewModel::class.java)
    }

    private fun setupUI() {
        setupActionBar()
        rv_users.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf()) { user ->
            user.let {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intent)
            }
        }

        rv_users.addItemDecoration(
            DividerItemDecoration(
                rv_users.context,
                (rv_users.layoutManager as LinearLayoutManager).orientation
            )
        )
        rv_users.adapter = adapter
    }

    private fun setupActionBar() {
        val title = "Github User's"
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        setupSearchView()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                return true
            }
            else -> return true
        }
    }

    private fun setupSearchView() {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                keyword = query.trim()
                setupObservers()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setupObservers() {
        viewModel.searchUsers(keyword).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        rv_users.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                        resource.data?.let { users -> setupData(users) }
                    }
                    Status.ERROR -> {
                        rv_users.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                        it.message?.let { it1 -> this.toast(it1) }
                    }
                    Status.LOADING -> {
                        shimmer_view_container.startShimmer()
                        shimmer_view_container.visibility = View.VISIBLE
                        rv_users.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setupData(users: List<User>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
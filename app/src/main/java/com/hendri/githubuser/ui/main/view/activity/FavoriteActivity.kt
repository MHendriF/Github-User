package com.hendri.githubuser.ui.main.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelperImp
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.local.DatabaseBuilder
import com.hendri.githubuser.data.local.DatabaseHelperImp
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.adapter.MainAdapter
import com.hendri.githubuser.ui.main.viewmodel.FavoriteViewModel
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.rvUsers
import kotlinx.android.synthetic.main.activity_main.shimmerContainer
import kotlinx.android.synthetic.main.fragment_followers.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImp(RetrofitBuilder.apiService),
                DatabaseHelperImp(DatabaseBuilder.getInstance(applicationContext))
            )
        ).get(FavoriteViewModel::class.java)
    }

    private fun setupUI() {
        setupActionBar()
        rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf()) { user ->
            user.let {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intent)
            }
        }
        rvUsers.addItemDecoration(
            DividerItemDecoration(
                rvUsers.context,
                (rvUsers.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvUsers.adapter = adapter

        shimmerContainer.stopShimmer()
        shimmerContainer.visibility = View.GONE
    }

    private fun setupObservers() {
        viewModel.fetchUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        rvUsers.visibility = View.VISIBLE
                        shimmerContainer.stopShimmer()
                        shimmerContainer.visibility = View.GONE
                        resource.data?.let { users -> setupData(users) }
                    }
                    Status.ERROR -> {
                        rvUsers.visibility = View.VISIBLE
                        shimmerContainer.stopShimmer()
                        shimmerContainer.visibility = View.GONE
                        it.message?.let { it1 -> this.toast(it1) }
                    }
                    Status.LOADING -> {
                        shimmerContainer.startShimmer()
                        rvUsers.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setupActionBar() {
        val title = "Favorite User's"
        supportActionBar?.title = title
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
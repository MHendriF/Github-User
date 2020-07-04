package com.hendri.githubuser.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.api.UserResponse
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.adapter.MainAdapter
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        adapter = MainAdapter(arrayListOf())
        rv_users.addItemDecoration(
            DividerItemDecoration(
                rv_users.context,
                (rv_users.layoutManager as LinearLayoutManager).orientation
            )
        )
        rv_users.adapter = adapter
    }

    private fun setupActionBar(){
        if (supportActionBar != null) {
            val title = "Github User's"
            supportActionBar!!.title = title
        }
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        rv_users.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                        resource.data?.let {
                                users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        rv_users.visibility = View.VISIBLE
                        shimmer_view_container.stopShimmer()
                        shimmer_view_container.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        shimmer_view_container.startShimmer()
                        rv_users.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<User>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}
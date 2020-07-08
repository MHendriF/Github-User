package com.hendri.githubuser.ui.main.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.adapter.FollowingAdapter
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.activity_main.*


class FollowingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: FollowingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(MainViewModel::class.java)
    }

    private fun setupUI() {
        rv_users.layoutManager = LinearLayoutManager(activity)
        adapter = FollowingAdapter(arrayListOf()) { user ->
            user.let {

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

    private fun setupObservers() {
        activity?.let {
            viewModel.getFollowing().observe(it, Observer {
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
                            it.message?.let { it1 -> activity?.toast(it1) }
                        }
                        Status.LOADING -> {
                            shimmer_view_container.startShimmer()
                            rv_users.visibility = View.GONE
                        }
                    }
                }
            })
        }
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
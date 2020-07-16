package com.hendri.githubuser.ui.main.view.fragment

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
import com.hendri.githubuser.data.api.ApiHelperImp
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.local.DatabaseBuilder
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.local.DatabaseHelperImp
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.adapter.FollowersAdapter
import com.hendri.githubuser.ui.main.view.activity.DetailActivity
import com.hendri.githubuser.ui.main.viewmodel.FollowersViewModel
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.fragment_followers.*


class FollowersFragment : Fragment() {

    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: FollowersAdapter
    private lateinit var user: User

    companion object {
        private const val ARG_USER = "arg_user"

        fun newInstance(user: User) = FollowersFragment().withArgs {
            putParcelable(ARG_USER, user)
        }

        private inline fun <T : Fragment> T.withArgs(
            argsBuilder: Bundle.() -> Unit
        ): T = this.apply {
            arguments = Bundle().apply(argsBuilder)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupData() {
        if (arguments != null) {
            user = arguments?.getParcelable<User>(ARG_USER) as User
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImp(RetrofitBuilder.apiService),
                DatabaseHelperImp(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(FollowersViewModel::class.java)
    }

    private fun setupUI() {
        rvUsers.layoutManager = LinearLayoutManager(requireContext())
        adapter = FollowersAdapter(arrayListOf()) { user ->
            user.let {
                val intent = Intent(requireContext(), DetailActivity::class.java)
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
    }

    private fun setupObservers() {
        activity?.let {
            viewModel.getFollowers(user.login).observe(it, Observer {
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
                            it.message?.let { it1 -> requireContext().toast(it1) }
                        }
                        Status.LOADING -> {
                            shimmerContainer.startShimmer()
                            rvUsers.visibility = View.GONE
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
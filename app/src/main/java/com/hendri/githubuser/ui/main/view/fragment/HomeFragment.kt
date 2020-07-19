package com.hendri.githubuser.ui.main.view.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.hendri.githubuser.ui.main.view.activity.DetailActivity
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchManager: SearchManager
    private lateinit var keyword: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupActionBar()
        setupViewModel()
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        setupSearchView()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_setting -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                true
            }
            R.id.action_favorite -> {
                findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
                true
            }
            else -> true
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImp(RetrofitBuilder.apiService),
                DatabaseHelperImp(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        rvUsers.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf()) { user ->
            user.let {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
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

    private fun setupSearchView() {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
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
        viewModel.searchUsers(keyword).observe(requireActivity(), Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        rvUsers.visibility = View.VISIBLE
                        shimmerContainer.stopShimmer()
                        shimmerContainer.visibility = View.GONE
                        if (resource.data?.size == 0) {
                            ivNotFound.visibility = View.VISIBLE
                            tvNotFound.visibility = View.VISIBLE
                        } else{
                            ivNotFound.visibility = View.GONE
                            tvNotFound.visibility = View.GONE
                        }
                        resource.data?.let { users -> setupData(users) }
                    }
                    Status.ERROR -> {
                        rvUsers.visibility = View.VISIBLE
                        shimmerContainer.stopShimmer()
                        shimmerContainer.visibility = View.GONE
                        ivNotFound.visibility = View.GONE
                        tvNotFound.visibility = View.GONE
                        it.message?.let { it1 -> requireContext().toast(it1) }
                    }
                    Status.LOADING -> {
                        shimmerContainer.startShimmer()
                        shimmerContainer.visibility = View.VISIBLE
                        rvUsers.visibility = View.GONE
                        ivSearch.visibility = View.GONE
                        tvSearch.visibility = View.GONE
                        ivNotFound.visibility = View.GONE
                        tvNotFound.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_home_fragment)
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
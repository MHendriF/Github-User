package com.hendri.githubuser.ui.main.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_search.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

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
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        searchUser(edtSearch.text.toString().trim())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
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
        viewModel = ViewModelProvider(this, ViewModelFactory(
                ApiHelperImp(RetrofitBuilder.apiService),
                DatabaseHelperImp(DatabaseBuilder.getInstance(requireContext().applicationContext)),
                requireContext(),
                requireActivity().application
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

    private fun setupObservers(keyword: String) {
        viewModel.searchUsers(keyword).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        shimmerContainer.stopShimmer()
                        shimmerContainer.visibility = View.GONE
                        if (resource.data?.size == 0) {
                            backdropEmpty.visibility = View.VISIBLE
                        } else{
                            rvUsers.visibility = View.VISIBLE
                            backdropEmpty.visibility = View.GONE
                        }
                        resource.data?.let { users -> setupData(users) }
                    }
                    Status.ERROR -> {
                        shimmerContainer.stopShimmer()
                        shimmerContainer.visibility = View.GONE
                        it.message?.let { it1 -> requireContext().toast(it1) }
                    }
                    Status.LOADING -> {
                        shimmerContainer.startShimmer()
                        shimmerContainer.visibility = View.VISIBLE
                        rvUsers.visibility = View.GONE
                        backdropSearch.visibility = View.GONE
                        backdropEmpty.visibility = View.GONE
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

    private fun searchUser(keyword: String) {
        if(!edtSearch.text.isBlank()){
            rvUsers.requestFocus()
            requireContext().hideKeyboard(requireView())
            setupObservers(keyword)
        }
    }

    private fun setupSearch() {
        btnSearch.setOnClickListener {
            searchUser(edtSearch.text.toString().trim())
        }

        edtSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUser(edtSearch.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
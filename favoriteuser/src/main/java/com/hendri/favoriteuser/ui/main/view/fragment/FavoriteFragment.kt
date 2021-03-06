package com.hendri.favoriteuser.ui.main.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hendri.favoriteuser.R
import com.hendri.favoriteuser.data.model.User
import com.hendri.favoriteuser.ui.base.ViewModelFactory
import com.hendri.favoriteuser.ui.main.adapter.FavoriteAdapter
import com.hendri.favoriteuser.ui.main.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_empty.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        setupUI()
        setupViewModel()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                requireContext().applicationContext
            )
        ).get(FavoriteViewModel::class.java)
    }


    private fun setupObservers() {
        viewModel.getFavoriteUsers.observe(viewLifecycleOwner, Observer {
            it.let {
                if(it.isNotEmpty()){
                    setupData(it)
                    rvUsers.visibility = View.VISIBLE
                } else {
                    tvNotFound.text = getString(R.string.empty_favorite)
                    backdropEmpty.visibility = View.VISIBLE
                    rvUsers.visibility = View.GONE
                }
            }
        })
    }

    private fun setupUI() {
        rvUsers.layoutManager = LinearLayoutManager(requireActivity())
        adapter = FavoriteAdapter(arrayListOf())
        rvUsers.addItemDecoration(
            DividerItemDecoration(
                rvUsers.context,
                (rvUsers.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvUsers.adapter = adapter

    }


    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_favorite_fragment)
    }

    private fun setupData(users: List<User>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }
}
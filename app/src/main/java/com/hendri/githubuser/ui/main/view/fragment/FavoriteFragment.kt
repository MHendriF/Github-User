package com.hendri.githubuser.ui.main.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelperImp
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.local.DatabaseBuilder
import com.hendri.githubuser.data.local.DatabaseHelperImp
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.adapter.FavoriteAdapter
import com.hendri.githubuser.ui.main.view.activity.DetailActivity
import com.hendri.githubuser.ui.main.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

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
        setupItemTouch()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImp(RetrofitBuilder.apiService),
                DatabaseHelperImp(DatabaseBuilder.getInstance(requireContext().applicationContext)),
                requireContext().applicationContext,
                requireActivity().application
            )
        ).get(FavoriteViewModel::class.java)
    }


    private fun setupObservers() {
        viewModel.favoriteUsers.observe(viewLifecycleOwner, Observer {
            it.let {
                Timber.d("Repo :: observer(${it.size})")
                favoriteAdapter.submitList(it)
            }
        })
    }

    private fun setupUI() {
        rvUsers.layoutManager = LinearLayoutManager(requireActivity())
        favoriteAdapter = FavoriteAdapter { user ->
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
        rvUsers.adapter = favoriteAdapter

        rvUsers.visibility = View.VISIBLE
        shimmerContainer.stopShimmer()
        shimmerContainer.visibility = View.GONE
    }


    private fun setupItemTouch() {
        // Add the functionality to swipe items in the
        // recycler view to delete that item
        val helper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition
                    val user: User? = favoriteAdapter.getUserAtPosition(position)

                    // Delete the user
                    requireContext().toast("Delete user ${user?.login} from favorite")
                    if (user != null) {
                        viewModel.deleteUserById(user)
                    }
                }
            })
        helper.attachToRecyclerView(rvUsers)
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_favorite_fragment)
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
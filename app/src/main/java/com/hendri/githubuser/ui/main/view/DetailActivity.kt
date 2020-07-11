package com.hendri.githubuser.ui.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.fragment.FollowersFragment
import com.hendri.githubuser.ui.main.fragment.FollowingFragment
import com.hendri.githubuser.ui.main.viewmodel.MainViewModel
import com.hendri.githubuser.utils.Status
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.math.abs

class DetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private val PERCENTAGE_TO_ANIMATE_AVATAR = 20
    private var mIsAvatarShown = true
    private var mMaxScrollSize = 0
    private lateinit var viewModel: MainViewModel
    private lateinit var user: User
    private lateinit var following: String
    private lateinit var followers: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupActionBar()

        user = intent.getParcelableExtra(EXTRA_USER)!!
        followers = resources.getString(R.string.followers)
        following = resources.getString(R.string.following)

        setupViewModel()
        setupObservers()

        toolbar.setNavigationOnClickListener { onBackPressed() }
        appbarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = appbarLayout.totalScrollRange

        val adapter = TabAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            user,
            followers,
            following
        )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

    class TabAdapter(
        fm: FragmentManager,
        behavior: Int,
        private val user: User,
        followers: String,
        following: String
    ) :
        FragmentStatePagerAdapter(fm, behavior) {
        private val tabName: Array<String> = arrayOf(followers, following)

        override fun getItem(position: Int): Fragment {

            val fragment: Fragment?
            fragment = when (position) {
                0 -> FollowersFragment.newInstance(user)
                1 -> FollowingFragment.newInstance(user)
                else -> FollowersFragment.newInstance(user)
            }
            return fragment
        }

        override fun getCount(): Int = 2
        override fun getPageTitle(position: Int): CharSequence? = tabName[position]
    }

    private fun setupActionBar() {
        val title = "Detail User"
        supportActionBar?.title = title
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (mMaxScrollSize == 0) mMaxScrollSize = appBarLayout!!.totalScrollRange

        val percentage: Int = abs(verticalOffset) * 100 / mMaxScrollSize

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false
            avatarImage?.animate()
                ?.scaleY(0f)?.scaleX(0f)
                ?.setDuration(200)
                ?.start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true
            avatarImage?.animate()
                ?.scaleY(1f)?.scaleX(1f)
                ?.start()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(MainViewModel::class.java)
    }

    private fun setupUI(user: User) {
        tv_item_name.text = user.name
        tv_item_email.text = user.email
        tv_item_bio.text = user.bio
        tv_item_repo.text = user.public_repos.toString()
        tv_item_follower.text = user.followers.toString()
        tv_item_following.text = user.following.toString()
        Glide.with(avatarImage.context)
            .load(user.avatar_url)
            .into(avatarImage)
    }

    private fun setupObservers() {
        viewModel.detailUser(user.login).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressbar.visibility = View.GONE
                        resource.data?.let { user -> setupUI(user) }
                    }
                    Status.ERROR -> {
                        progressbar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressbar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}
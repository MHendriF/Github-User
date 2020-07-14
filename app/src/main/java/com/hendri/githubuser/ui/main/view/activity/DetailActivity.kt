package com.hendri.githubuser.ui.main.view.activity

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.hendri.githubuser.R
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.api.RetrofitBuilder
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.base.ViewModelFactory
import com.hendri.githubuser.ui.main.view.fragment.FollowersFragment
import com.hendri.githubuser.ui.main.view.fragment.FollowingFragment
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
        val tabsName = arrayOf(
            resources.getString(R.string.followers),
            resources.getString(R.string.following)
        )

        setupViewModel()
        setupObservers()

        toolbar.setNavigationOnClickListener { onBackPressed() }
        appbarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = appbarLayout.totalScrollRange

        val adapter =
            TabAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                user,
                tabsName
            )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    class TabAdapter(
        fm: FragmentManager,
        behavior: Int,
        private val user: User,
        private val tabName: Array<String>
    ) :
        FragmentStatePagerAdapter(fm, behavior) {

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
            ivAvatar?.animate()
                ?.scaleY(0f)?.scaleX(0f)
                ?.setDuration(200)
                ?.start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true
            ivAvatar?.animate()
                ?.scaleY(1f)?.scaleX(1f)
                ?.start()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(MainViewModel::class.java)
    }

    private fun setupUI(user: User) {
        tvName.text = user.name
        tvUsername.text = user.login
        if (user.bio.isNullOrEmpty()) {
            tvBio.visibility = View.GONE
        } else {
            tvBio.visibility = View.VISIBLE
            tvBio.text = user.bio
        }

        tvRepo.text = user.public_repos.toString()
        tvFollowers.text = user.followers.toString()
        tvFollowing.text = user.following.toString()
        Glide.with(ivAvatar.context)
            .load(user.avatar_url)
            .apply(
                RequestOptions
                    .circleCropTransform()
                    .override(100, 100)
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
            .into(ivAvatar)
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
package com.hendri.githubuser.ui.main.view

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.hendri.githubuser.R
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.ui.main.fragment.FollowersFragment
import com.hendri.githubuser.ui.main.fragment.FollowingFragment
import kotlinx.android.synthetic.main.activity_detail.*
import kotlin.math.abs

class DetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private val PERCENTAGE_TO_ANIMATE_AVATAR = 20
    private var mIsAvatarShown = true
    private var mMaxScrollSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupActionBar()

        val user = intent.getParcelableExtra(EXTRA_USER) as User
        val text = "Name : ${user.name.toString()},\nEmail : ${user.email},\nId : ${user.id},\nLogin : ${user.login}"
        Log.d("Trace", "onCreate: " + text)

        appbarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = appbarLayout.totalScrollRange

        val adapter = TabAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    class TabAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
        private val tabName : Array<String> = arrayOf("Followers", "Following")

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> FollowersFragment()
        }

        override fun getCount(): Int = 2
        override fun getPageTitle(position: Int): CharSequence? = tabName.get(position)
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

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR&& !mIsAvatarShown) {
            mIsAvatarShown = true
            avatarImage?.animate()
                ?.scaleY(1f)?.scaleX(1f)
                ?.start()
        }
    }
}
package com.hendri.githubuser.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hendri.githubuser.R
import com.hendri.githubuser.data.model.User

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setupActionBar()

        val user = intent.getParcelableExtra(EXTRA_USER) as User
        val text = "Name : ${user.name.toString()},\nEmail : ${user.email},\nId : ${user.id},\nLogin : ${user.login}"
        Log.d("Trace", "onCreate: "+text)
    }

    private fun setupActionBar() {
        if (supportActionBar != null) {
            val title = "Detail User"
            supportActionBar!!.title = title
        }
    }
}
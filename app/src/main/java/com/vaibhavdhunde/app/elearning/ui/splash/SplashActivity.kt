package com.vaibhavdhunde.app.elearning.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vaibhavdhunde.app.elearning.MainActivity
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.ActivitySplashBinding
import com.vaibhavdhunde.app.elearning.ui.login.LoginActivity
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.startFreshActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupNavigation()
        loadUser()
    }

    private fun setupDataBinding() {
        splashViewModel = obtainViewModel(SplashViewModel::class.java, factory)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.apply {
            viewmodel = splashViewModel
            lifecycleOwner = this@SplashActivity
        }
    }

    private fun setupNavigation() {
        binding.viewmodel?.mainEvent?.observe(this, EventObserver {
            navigateToMainActivity()
        })

        binding.viewmodel?.loginEvent?.observe(this, EventObserver {
            navigateToLoginActivity()
        })
    }

    private fun loadUser() {
        binding.viewmodel?.loadUser()
    }

    private fun navigateToMainActivity() {
        startFreshActivity(MainActivity::class.java)
    }

    private fun navigateToLoginActivity() {
        startFreshActivity(LoginActivity::class.java)
    }
}

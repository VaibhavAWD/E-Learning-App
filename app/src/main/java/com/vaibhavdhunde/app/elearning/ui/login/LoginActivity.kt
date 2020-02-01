package com.vaibhavdhunde.app.elearning.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import com.vaibhavdhunde.app.elearning.MainActivity
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.ActivityLoginBinding
import com.vaibhavdhunde.app.elearning.ui.register.RegisterActivity
import com.vaibhavdhunde.app.elearning.util.*
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupInputField()
        setupEvents()
        setupNavigation()
    }

    private fun setupDataBinding() {
        loginViewModel = obtainViewModel(LoginViewModel::class.java, factory)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.apply {
            viewmodel = loginViewModel
            lifecycleOwner = this@LoginActivity
        }
    }

    private fun setupInputField() {
        input_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginUser()
                true
            } else {
                false
            }
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(this, EventObserver { message ->
            if (message is Int) {
                activity_login.snackbar(message)
            } else if (message is String) {
                activity_login.snackbar(message)
            }
        })

        binding.viewmodel?.closeSoftKeyboardEvent?.observe(this, EventObserver {
            closeSoftKeyboard()
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.mainEvent?.observe(this, EventObserver {
            navigateToMainActivity()
        })

        binding.viewmodel?.registerEvent?.observe(this, EventObserver {
            navigateToRegisterActivity()
        })
    }

    private fun loginUser() {
        binding.viewmodel?.loginUser()
    }

    private fun navigateToMainActivity() {
        startFreshActivity(MainActivity::class.java)
    }

    private fun navigateToRegisterActivity() {
        startActivity(RegisterActivity::class.java)
    }

}

package com.vaibhavdhunde.app.elearning.ui.register

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.vaibhavdhunde.app.elearning.MainActivity
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.ActivityRegisterBinding
import com.vaibhavdhunde.app.elearning.util.*
import kotlinx.android.synthetic.main.activity_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RegisterActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        setupInputField()
        setupEvents()
        setupNavigation()
    }

    private fun setupDataBinding() {
        registerViewModel = obtainViewModel(RegisterViewModel::class.java, factory)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.apply {
            viewmodel = registerViewModel
            lifecycleOwner = this@RegisterActivity
        }
    }

    private fun setupInputField() {
        input_conf_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                registerUser()
                true
            } else {
                false
            }
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(this, EventObserver { message ->
            if (message is Int) {
                activity_register.snackbar(message)
            } else if (message is String) {
                activity_register.snackbar(message)
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

        binding.viewmodel?.loginEvent?.observe(this, EventObserver {
            navigateToLoginActivity()
        })
    }

    private fun registerUser() {
        binding.viewmodel?.registerUser()
    }

    private fun navigateToMainActivity() {
        startFreshActivity(MainActivity::class.java)
    }

    private fun navigateToLoginActivity() {
        finish()
    }

}

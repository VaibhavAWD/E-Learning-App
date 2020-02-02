package com.vaibhavdhunde.app.elearning.ui.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentChangePasswordBinding
import com.vaibhavdhunde.app.elearning.ui.login.LoginActivity
import com.vaibhavdhunde.app.elearning.util.*
import kotlinx.android.synthetic.main.fragment_change_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ChangePasswordFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        changePasswordViewModel = obtainViewModel(ChangePasswordViewModel::class.java, factory)

        binding = FragmentChangePasswordBinding.bind(view).apply {
            viewmodel = changePasswordViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupInputField()
        setupEvents()
        setupNavigation()
    }

    private fun setupInputField() {
        input_conf_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                updatePassword()
                true
            } else {
                false
            }
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            if (message is Int) {
                fragment_change_password.snackbar(message)
            } else if (message is String) {
                fragment_change_password.snackbar(message)
            }
        })

        binding.viewmodel?.closeSoftKeyboardEvent?.observe(viewLifecycleOwner, EventObserver {
            closeSoftKeyboard()
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.loginEvent?.observe(viewLifecycleOwner, EventObserver {
            navigateToLoginActivity()
        })
    }

    private fun updatePassword() {
        binding.viewmodel?.updatePassword()
    }

    private fun navigateToLoginActivity() {
        startFreshActivity(LoginActivity::class.java)
    }

}
